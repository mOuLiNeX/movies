package fr.manu.experiments.domain

import java.io.File
import java.util.regex.Pattern

data class Movie(val movieId: Long, val title: String, val genres: List<Genre>)

enum class Genre(val text: String) {
    Action("Action"),
    Adventure("Adventure"),
    Animation("Animation"),
    Children("Children"),
    Comedy("Comedy"),
    Crime("Crime"),
    Documentary("Documentary"),
    Drama("Drama"),
    Fantasy("Fantasy"),
    FilmNoir("Film-Noir"),
    Horror("Horror"),
    Musical("Musical"),
    Mystery("Mystery"),
    Romance("Romance"),
    SciFi("Sci-Fi"),
    Thriller("Thriller"),
    War("War"),
    Western("Western"),
    IMAX("IMAX");

    companion object {
        fun from(asText: String) =
            values().find { it.text == asText } ?: throw RuntimeException("No genre with text $asText")
    }
}

private val movies by lazy {
    val lines = File(Movie::class.java.classLoader.getResource("movies.csv").file)
        .readLines()
        .drop(1)

    lines.filterNot { it.contains("\"") }.map { line -> line.split(",") }
        .map {
            Movie(
                it[0].toLong(),
                it[1],
                if (it[2] == "(no genres listed)") emptyList() else it[2].split("|").map { Genre.from(it) }
            )
        } union
            lines.filter { it.contains("\"") }
                .map { line -> line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()) }
                .map {
                    Movie(
                        it[0].toLong(),
                        it[1],
                        if (it[2] == "(no genres listed)") emptyList() else it[2].split("|").map { Genre.from(it) }
                    )
                }
}

fun findMovieBy(movieId: Long) = movies.find { it.movieId == movieId }

fun findRandomMovie() = movies.random()