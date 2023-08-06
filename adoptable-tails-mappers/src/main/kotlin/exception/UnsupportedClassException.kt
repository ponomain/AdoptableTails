package ru.otus.otuskotlin.adoptabletails.mappers.exception

class UnsupportedClassException(javaClass: Class<*>) : Throwable("Class $javaClass cannot be mapped to context")