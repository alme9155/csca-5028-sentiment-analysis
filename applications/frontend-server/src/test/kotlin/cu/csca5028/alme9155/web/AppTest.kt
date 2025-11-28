package cu.csca5028.alme9155.web

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

class AppTest {
    @Test
    fun testEmptyHome() = testApplication {
        application { 
            frontendModule() 
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(
            body.isNotBlank(),
            "Home page body should not be blank"
        )
    }

    @Test
    fun testHealth() = testApplication {
        application { frontendModule() }

        val response = runBlocking { client.get("/health") }
        assertEquals(HttpStatusCode.OK, response.status)

        val body = runBlocking { response.bodyAsText() }
        assertEquals("OK", body)
    }

    @Test
    fun testMetrics() = testApplication {
        application { frontendModule() }

        val response = runBlocking { client.get("/metrics") }
        assertEquals(HttpStatusCode.OK, response.status)

        val body = runBlocking { response.bodyAsText() }
        assertContains(body, "app_uptime_seconds{service=\"frontend-server\"")
    }    
}
