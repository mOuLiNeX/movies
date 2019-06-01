package fr.manu.experiments.domain

import fr.manu.experiments.infra.gaussianRandomTimer
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

data class Tag(val userId: Long, val movieId: Long, val tag: String, val timestamp: LocalDateTime)

private val tags by lazy {
    File(Movie::class.java.classLoader.getResource("tags.csv").file)
        .readLines()
        .drop(1)
        .map { line -> line.split(",") }
        .map {
            Tag(
                it[0].toLong(),
                it[1].toLong(),
                it[2],
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it[3].toLong()), ZoneId.systemDefault())
            )
        }.toSet()
}

fun findTagBy(movieId: Long): Tag? {
    TimeUnit.MILLISECONDS.sleep(gaussianRandomTimer(200, 25))
    return tags.find { it.movieId == movieId }
}