import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val url = "http://localhost:8080/random"

  val httpProtocol = http
    .baseUrl(url)
    .acceptHeader("application/json")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatling")

  val scn = scenario("BasicSimulation")
    .exec(http("request random").get("/"))

  setUp(
    scn.inject(
      rampConcurrentUsers(0) to (200) during (10 seconds),
      constantConcurrentUsers(200) during (30 seconds),
      rampConcurrentUsers(200) to (300) during (10 seconds),
      constantConcurrentUsers(300) during (10 seconds),
    )
  ).protocols(httpProtocol)
}