package com.sample.messages

import com.sun.net.httpserver.HttpServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux
import java.net.InetSocketAddress

@EnableWebFlux
@SpringBootApplication
class MessagesApplication

fun main(args: Array<String>) {

    // starts the fake external service on `localhost:8181`
    val server = HttpServer.create(InetSocketAddress(8181), 0)
    addEndpoint(server, "/fake_external_service")
    Thread(Runnable { server.start() }).start()

    // starts the application
    runApplication<MessagesApplication>(*args)
}

private fun addEndpoint(server: HttpServer, uri: String) {
    server.createContext(uri) { httpExchange ->

        val response = "{\"result\": \"ok\"}"
        val statusCode = 200

        val responseBytes = response.toByteArray()
        httpExchange.sendResponseHeaders(statusCode, responseBytes.size.toLong())
        httpExchange.responseBody.use { os -> os.write(responseBytes) }
    }
}
