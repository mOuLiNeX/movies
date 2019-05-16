package fr.manu.experiments.infra

import fr.manu.experiments.infra.Color.*
import fr.manu.experiments.infra.Symbol.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

private enum class Color(val ascii: String) {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m");

    fun colorize(text: String) = "$ascii$text\u001B[0m"
}

private enum class Symbol(val unicode: String) {
    MAN("\uD83D\uDE10"),
    DOG("\uD83D\uDC36"),
    CAT("\uD83D\uDC31"),
    RABBIT("\uD83D\uDC30");
}


private val symbols = listOf(MAN to RED, DOG to GREEN, CAT to BLUE, RABBIT to PURPLE)

private fun defineThreadName(namePrefix: String, numThread: Int): String {
    val (symbol, color) = symbols[numThread]
    return color.colorize("${symbol.unicode} $namePrefix-$numThread")
}


private fun buildThreadFactory(namePrefix: String): ThreadFactory {
    val default = Executors.defaultThreadFactory()
    if (namePrefix.isEmpty()) {
        return default
    }
    val count = AtomicLong(0)

    return ThreadFactory { runnable ->
        val thread = default.newThread(runnable)
        thread.name = defineThreadName(namePrefix, count.getAndIncrement().toInt())
        thread
    }
}

fun buildThreadPoolExecutor(nbThread: Int, poolName: String) =
    if (nbThread > 4) throw RuntimeException("Faut pas exagérer quand même")
    else Executors.newFixedThreadPool(nbThread, buildThreadFactory(poolName))

val directExecutor = Executor { command: Runnable -> command.run() }