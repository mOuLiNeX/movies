package fr.manu.experiments

import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import java.time.Instant.*
import java.time.temporal.ChronoUnit.*
import kotlin.concurrent.timer


fun main() {
    val client = OkHttp()

    timer(period = 1000) {
        val start = now()
        val response = client(Request(Method.GET, "http://localhost:8080/random"))
        when (response.status) {
            OK -> println("Random [${MILLIS.between(start, now())} ms] => ${response.bodyString()}")
            else -> println("Request failed [${MILLIS.between(start, now())} ms]")
        }
    }
}
