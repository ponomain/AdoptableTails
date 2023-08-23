package ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers

import ru.otus.otuskotlin.adoptabletails.lib.cor.ICorExec
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.CorDslMarker
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.CorExecDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.ICorExecDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.handlers.CorChain
import ru.otus.otuskotlin.adoptabletails.lib.cor.handlers.executeSequential

@CorDslMarker
class CorChainDsl<T>() : CorExecDsl<T>() {
    override var title: String = ""
    override var description: String = ""
    private var handlers: MutableList<ICorExecDsl<T>> = mutableListOf()
    var strategy: suspend (T, List<ICorExec<T>>) -> Unit = ::executeSequential
    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        handles = handlers.map { it.build() },
        blockOn = blockOn,
        strategy = strategy,
        blockException = blockException
    )

    fun add(worker: ICorExecDsl<T>) {
        handlers.add(worker)
    }
}