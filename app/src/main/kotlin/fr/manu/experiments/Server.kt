package fr.manu.experiments

import fr.manu.experiments.domain.*
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.http4k.format.Jackson.auto
import org.http4k.server.Netty
import org.http4k.server.asServer

private val LOGGER: Logger = LoggerFactory.getLogger("fr.manu.experiments.movies.MainKt")

fun main() {
    val app: HttpHandler = routes(
        "/ping" bind GET to { _: Request -> Response(OK).body("pong!") },
        "/random" bind GET to { req: Request ->
            Response(OK).with(Body.auto<CompleteMovie>().toLens() of randomMovie())
        }
    )

    app.asServer(Netty(8080)).start()
}

data class CompleteMovie(val movie: Movie, val ratings: Rating?, val tag: Tag?)

private fun randomMovie(): CompleteMovie {
    val movie = findRandomMovie()
    with(movie) {
        return CompleteMovie(
            this,
            findRatingBy(this.movieId),
            findTagBy(this.movieId)
        )
    }
}
