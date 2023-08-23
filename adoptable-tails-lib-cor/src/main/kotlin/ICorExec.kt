package ru.otus.otuskotlin.adoptabletails.lib.cor

interface ICorExec<T> {
    val title :String
    val description :String
    suspend fun exec(context :T)
}