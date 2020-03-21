package de.lostmekka.covidjam.backend

import com.google.gson.Gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            testRequest(LevelGeneration, LevelGenerationEvent(levelSize = Dimension(10, 10)))
        }
    }
}

private fun <T: Any, U: Any> TestApplicationEngine.testRequest(
    endpoint: BackendEndpoint<T, U>,
    input: T
) {
    handleRequest(HttpMethod.Post, "/backend/${endpoint.path}") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Gson().toJson(input))
    }.apply {
        assertEquals(HttpStatusCode.OK, response.status())
        val answer = Gson().fromJson(response.content, List::class.java)
        println(answer)
    }
}
