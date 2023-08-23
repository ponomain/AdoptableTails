package ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers

import ru.otus.otuskotlin.adoptabletails.lib.cor.ICorExec
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.CorDslMarker
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.CorExecDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.handlers.CorWorker

@CorDslMarker
class CorWorkerDsl<T>(
    var blockHandle: suspend T.() -> Unit = {},
) : CorExecDsl<T>() {
    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockHandle = blockHandle,
        blockOn = blockOn,
        blockException = blockException
    )
}