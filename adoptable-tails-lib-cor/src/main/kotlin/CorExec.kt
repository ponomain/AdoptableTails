package ru.otus.otuskotlin.adoptabletails.lib.cor

abstract class CorExec<T>(
    override val title: String,
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = {true},
    private val blockException: suspend T.(Throwable) -> Unit = {throw it}
) : ICorExec<T> {

    protected abstract suspend fun handle(context: T)

    override suspend fun exec(context: T) {
        if (context.blockOn()) {
            try {
                handle(context)
            } catch (e: Throwable) {
                context.blockException(e)
            }
        }
    }
}