package ru.otus.otuskotlin.adoptabletails.lib.cor

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.chain
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.parallel
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.sequential
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker
import ru.otus.otuskotlin.adoptabletails.lib.cor.handlers.CorChain
import ru.otus.otuskotlin.adoptabletails.lib.cor.handlers.CorWorker

enum class TestStatus { NONE, RUNNING }

data class TestContext(
    var workName: String = "",
    var count: Int = 0,
    var workStatus: TestStatus = TestStatus.NONE,
    var nullableString: String? = null
)

class CorTest : FunSpec({

    test("CoR: basic handler test") {
        val ctx = TestContext("Result handler: ")
        val handler: CorWorker<TestContext> = CorWorker(
            title = "test №1",
            blockHandle = { workName += "result of one handle" }
        )
        handler.exec(ctx)
        ctx.workName shouldBe "Result handler: result of one handle"
    }

    test("CoR: handler off test") {
        val ctx = TestContext("Worker off")
        val handler: CorWorker<TestContext> = CorWorker(
            title = "test №2",
            blockOn = { workStatus == TestStatus.RUNNING },
            blockHandle = { workName += "result of one handle" }
        )
        handler.exec(ctx)
        ctx.workName shouldBe "Worker off"
    }

    test("CoR: testing a chain of multiple handlers") {
        val ctx = TestContext("Chain: ")
        val generateCtx: (String) -> CorWorker<TestContext> =
            { str -> CorWorker(blockHandle = { workName += str }, title = "multiHandle") }
        val handler: CorChain<TestContext> = CorChain(
            title = "test №3",
            handles = listOf(generateCtx("link one, "), generateCtx("link two"))
        )
        handler.exec(ctx)
        ctx.workName shouldBe "Chain: link one, link two"
    }

    test("CoR: testing exception handling") {
        val ctx = TestContext(workName = "Exception: ")
        val handler: CorWorker<TestContext> = CorWorker(
            title = "test №4",
            blockHandle = { count += nullableString!!.length },
            blockException = { if (it::class == NullPointerException::class) workName += "NPE exception" }
        )
        handler.exec(ctx)
        ctx.workName shouldBe "Exception: NPE exception"
    }

    /*dsl test*/

    test("CoR DSL: basic handler test") {
        val ctx = TestContext(workName = "Dsl create worker: ")
        val handler = worker<TestContext> {
            title = "test №5"
            handle { count += 21 }
        }.build()
        handler.exec(ctx)
        ctx.count shouldBe 21
    }

    test("CoR DSL: worker off") {
        val ctx = TestContext(workName = "dsl worker off")
        val handler = worker<TestContext> {
            title = "test №6"
            handle { workName += "result of one handler dsl" }
            on { false }
        }.build()
        handler.exec(ctx)
        ctx.workName shouldBe "dsl worker off"
    }


    test("CoR DSL: testing a chain of multiple handlers") {
        val ctx = TestContext(count = 0)
        val chain = chain<TestContext> {
            worker {
                title = "№6.1"
                handle { count += 1 }
                on { true }
            }
            worker {
                title = "№6.2"
                handle { count += 1 }
                on { true }
            }
        }.build()

        chain.exec(ctx)
        ctx.count shouldBe 2
    }


    test("CoR DSL: several mixed chains") {
        val ctx = TestContext(count = 0)
        val chain = chain<TestContext> {
            worker {
                title = "№7.1"
                handle { count += 1 }
                on { true }
            }
            worker {
                title = "№7.2"
                handle { count += 2 }
                on { count == 1 }
            }
            sequential {
                worker {
                    title = "№7.3"
                    handle { count += 3 }
                    on { count == 3 }
                }
                worker {
                    title = "№7.4"
                    handle { count += 99 }
                    on { false }
                }
                parallel {
                    worker {
                        title = "№7.5"
                        handle { count += 4 }
                        on { true }
                    }
                    worker {
                        title = "№7.6"
                        handle { count += 5 }
                        on { true }
                    }
                    worker("7.7") { count += 10 }
                    chain {
                        worker {
                            title = "№7.8"
                            handle { count += 5 }
                            on { true }
                        }
                        worker("7.9") { count += 15 }
                    }
                    sequential {
                        worker {
                            title = "№7.3"
                            handle { count += 5 }
                            on { true }
                        }
                    }


                }
            }
        }.build()

        chain.exec(ctx)
        ctx.count shouldBe 50
    }


})