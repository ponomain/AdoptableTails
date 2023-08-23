package ru.otus.otuskotlin.adoptabletails.lib.cor.handlers

import ru.otus.otuskotlin.adoptabletails.lib.cor.CorExec

class CorWorker<T>(
    title: String,
    description: String = "",
    private val blockHandle: suspend T.() -> Unit = {},
    blockOn: suspend T.() -> Boolean = { true },
    blockException: suspend T.(Throwable) -> Unit = {throw it}
) : CorExec<T>(title, description, blockOn, blockException) {

    override suspend fun handle(context: T) = blockHandle(context)

}