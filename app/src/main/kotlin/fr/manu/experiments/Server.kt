package fr.manu.experiments

import fr.manu.experiments.domain.*
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK
import org.eclipse.jetty.jmx.MBeanContainer
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.http4k.format.Jackson.auto
import org.http4k.server.Jetty
import org.http4k.server.asServer
import java.lang.management.ManagementFactory

private val LOGGER: Logger = LoggerFactory.getLogger("fr.manu.experiments.movies.MainKt")
private const val httpPort = 8080

fun main() {

    val app: HttpHandler = routes(
        "/ping" bind GET to { _: Request -> Response(OK).body("pong!") },
        "/random" bind GET to { req: Request ->
            Response(OK).with(Body.auto<CompleteMovie>().toLens() of findCompleteRandomMovie())
        }
    )

    // Setup Jetty
    val jetty = Server(QueuedThreadPool(15, 5, 10000)).apply {
        addConnector(ServerConnector(this).apply { port = httpPort })
    }
    // JMX monitoring
    jetty.addBean(MBeanContainer(ManagementFactory.getPlatformMBeanServer()))

    app.asServer(Jetty(httpPort, jetty)).start()
}