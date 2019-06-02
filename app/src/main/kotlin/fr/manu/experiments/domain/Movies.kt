package fr.manu.experiments.domain

import fr.manu.experiments.LOGGER
import fr.manu.experiments.infra.gaussianRandomTimer
import java.util.concurrent.TimeUnit

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
    val reader = Movie::class.java.classLoader.getResourceAsStream("movies.csv").bufferedReader()
    LOGGER.info("Loading movies from file")
    reader.use {
        val lines = reader.readLines().drop(1)

        lines.filterNot { it.contains("\"") }.map { line -> line.split(",") }
            .map {
                Movie(
                    it[0].toLong(),
                    it[1],
                    if (it[2] == "(no genres listed)") emptyList() else it[2].split("|").map {
                        Genre.from(
                            it
                        )
                    }
                )
            } union
                lines.filter { it.contains("\"") }
                    .map { line -> line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()) }
                    .map {
                        Movie(
                            it[0].toLong(),
                            it[1],
                            if (it[2] == "(no genres listed)") emptyList() else it[2].split("|").map {
                                Genre.from(
                                    it
                                )
                            }
                        )
                    }
    }
}

private fun findMovieBy(movieId: Long) = movies.find { it.movieId == movieId }

private fun findRandomMovie(): Movie {
    TimeUnit.MILLISECONDS.sleep(gaussianRandomTimer(50, 15))
    return movies.random()
}

data class CompleteMovie(val movie: Movie, val ratings: Rating?, val tag: Tag?)

fun findCompleteRandomMovie(): CompleteMovie {
    val movie = findRandomMovie()
    val rating = findRatingBy(movie.movieId)
    val tag = findTagBy(movie.movieId)
    return CompleteMovie(movie, rating, tag)
}