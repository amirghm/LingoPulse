package ai.lingopulse.sandbox

import ai.lingopulse.shared.APP_NAME
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.response.*
import java.io.File

fun startSandboxServer() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/$APP_NAME/{path...}") {
                val fullPath =
                    call.parameters.getAll("path")?.joinToString("/") ?: return@get call.respond(
                        HttpStatusCode.NotFound
                    )
                val file = File("/$fullPath") // your local file root here
                if (!file.exists()) return@get call.respond(HttpStatusCode.NotFound)

                val contentType = ContentType.parse(guessMimeType(fullPath))
                call.response.headers.append(HttpHeaders.ContentType, contentType.toString())
                call.respondFile(file)
            }
        }
    }.start(wait = false)
}

fun guessMimeType(path: String): String = when {
    path.endsWith(".html") -> "text/html"
    path.endsWith(".js") -> "application/javascript"
    path.endsWith(".css") -> "text/css"
    path.endsWith(".json") -> "application/json"
    path.endsWith(".png") -> "image/png"
    path.endsWith(".jpg") || path.endsWith(".jpeg") -> "image/jpeg"
    path.endsWith(".svg") -> "image/svg+xml"
    path.endsWith(".webp") -> "image/webp"
    path.endsWith(".ico") -> "image/x-icon"
    path.endsWith(".woff") -> "font/woff"
    path.endsWith(".woff2") -> "font/woff2"
    path.endsWith(".ttf") -> "font/ttf"
    path.endsWith(".mp4") -> "video/mp4"
    path.endsWith(".webm") -> "video/webm"
    path.endsWith(".ogg") -> "video/ogg"
    path.endsWith(".mp3") -> "audio/mpeg"
    path.endsWith(".wav") -> "audio/wav"
    path.endsWith(".wasm") -> "application/wasm"
    path.endsWith(".pdf") -> "application/pdf"
    path.endsWith(".zip") -> "application/zip"
    path.endsWith(".csv") -> "text/csv"
    else -> "application/octet-stream"
}
