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
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LevelGenerationTest {
    @BeforeTest
    fun setup() {
        IS_RUNNING_IN_TEST = true
    }

    @Test
    fun testRoot() {
        val input = LevelGenerationInput(
            levelSize = Dimension(50, 40)
        )
        repeat(10) {
            LevelGeneration.handleRequest(input)
        }
    }
}
