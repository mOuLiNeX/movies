package fr.manu.experiments.domain

import fr.manu.experiments.infra.gaussianRandomTimer
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

data class Rating(val userId: Long, val movieId: Long, val rating: Float, val timestamp: LocalDateTime)

private val ratings by lazy {
    File(Movie::class.java.classLoader.getResource("ratings.csv").file)
        .readLines()
        .drop(1)
        .map { line -> line.split(",") }
        .map {
            Rating(
                it[0].toLong(),
                it[1].toLong(),
                it[2].toFloat(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it[3].toLong()), ZoneId.systemDefault())
            )
        }.toSet()
}

fun findRatingBy(movieId: Long): Rating? {
    TimeUnit.MILLISECONDS.sleep(gaussianRandomTimer(200, 15))
    return ratings.find { it.movieId == movieId }
}