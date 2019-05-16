package fr.manu.experiments.domain

import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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

fun findRatingBy(movieId: Long) = ratings.find { it.movieId == movieId }