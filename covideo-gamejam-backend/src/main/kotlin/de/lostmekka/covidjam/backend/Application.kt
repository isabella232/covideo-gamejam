package de.lostmekka.covidjam.backend

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    IS_RUNNING_IN_TEST = testing

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        backendEndpoint(LevelGeneration)
    }
}

var IS_RUNNING_IN_TEST = false

interface BackendEndpoint<in T : Any, out U : Any> {
    val path: String
    fun handleRequest(input: T): U
}

private inline fun <reified T : Any, reified U : Any> Routing.backendEndpoint(endpoint: BackendEndpoint<T, U>) {
    post("/backend/${endpoint.path}") {
        val input = call.receive<T>()
        val output = endpoint.handleRequest(input)
        call.respond(output)
    }
}
