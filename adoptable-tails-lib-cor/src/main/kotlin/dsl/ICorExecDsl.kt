package ru.otus.otuskotlin.adoptabletails.lib.cor.dsl

import ru.otus.otuskotlin.adoptabletails.lib.cor.ICorExec

@CorDslMarker
interface ICorExecDsl<T> {
    var title :String
    var description :String
    fun build(): ICorExec<T>
}