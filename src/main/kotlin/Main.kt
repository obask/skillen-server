import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 9000, host = "127.0.0.1") {
        install(ContentNegotiation) {
            json(Json {
                classDiscriminator = "_t"
            })
        }

        routing {
            get("/") {
                call.respondText("Hello World 5", status = HttpStatusCode.OK)
            }
            get("/book") {
                val path = {}.javaClass.getResource("Dostoyevsky.txt")
                call.respond(tokenizeEnglishText(path?.readText() ?: TODO()))
            }
        }
    }.start(wait = true)
}
