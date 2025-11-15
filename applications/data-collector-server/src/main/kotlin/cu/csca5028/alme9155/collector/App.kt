package cu.csca5028.alme9155.collector

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.util.TimeZone

fun Application.collectorModule() {
    routing {
        get("/") {
            call.respondText("Sentiment Collector API", ContentType.Text.Plain)
        }
        get("/health") {
            call.respondText("OK", ContentType.Text.Plain)
        }
    }
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(
        factory = Netty,
        port = port, 
        module = Application::collectorModule
    ).start(wait = true)
}