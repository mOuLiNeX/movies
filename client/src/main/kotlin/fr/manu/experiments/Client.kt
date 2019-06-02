package fr.manu.experiments

import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import java.time.Instant.*
import java.time.temporal.ChronoUnit.*
import kotlin.concurrent.timer
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale.*


fun main() {

    val formatter =
        DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(FRENCH).withZone(ZoneId.systemDefault())

    val client = OkHttp()

    timer(period = 1000) {
        val start = now()
        val response = client(Request(Method.GET, "http://localhost:8080/random"))
        val timeInfo = "[@${formatter.format(start)} -> ${MILLIS.between(start, now())} ms]"
        when (response.status) {
            OK -> println(
                "Request OK $timeInfo => ${response.bodyString()}"
            )
            else -> println("Request failed $timeInfo => status = ${response.status}")
        }
    }
}
