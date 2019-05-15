package fr.manu.experiments

import fr.manu.experiments.infra.buildThreadPoolExecutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

private val LOGGER: Logger = LoggerFactory.getLogger("fr.manu.experiments.movies.MainKt")

fun main() {
    val executor = buildThreadPoolExecutor(4, "monPool")
    val tasks = (1..10).map {
        CompletableFuture.runAsync(
            Runnable {
                LOGGER.info("Coucou from a thread")
            },
            executor
        )
    }.toTypedArray()

    CompletableFuture.allOf(*tasks).get()

    executor.shutdownNow()

}