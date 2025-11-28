package cu.csca5028.alme9155.analyzer

import cu.csca5028.alme9155.database.*
import cu.csca5028.alme9155.sentiment.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlin.test.*

/** 
 * Unit test for data-analyzer service
 */
class AppTest {
    @Test
    fun testRoot() = testApplication {
        application { analyzerModule() }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "Data Analyzer")
    }

    @Test
    fun testHealth() = testApplication {
        application { analyzerModule() }

        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("OK", response.bodyAsText())
    }

    @Test
    fun testTopMoviesReturnsMockedData() = testApplication {
        mockkObject(MongoDBAdapter)
        try {
            every { MongoDBAdapter.getTopMovies() } returns listOf(
                MovieRatings("Best movie thriller ever created. Great script. Excellent acting.", 3.5),
                MovieRatings("Fight Club", 2.6)
            )

            application { analyzerModule() }

            val response = client.get("/top-movies")
            assertEquals(HttpStatusCode.OK, response.status)

            val body = response.bodyAsText()
            assertContains(body, "Best movie thriller")
            assertContains(body, "3.5")
            assertContains(body, "Fight Club")
        } finally {
            unmockkAll()
        }
    }


    @Test
    fun testAnalyzeRejectsBlankTitle() = testApplication {
        application { analyzerModule() }

        val response = client.post("/analyze") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                  "title": "   ",
                  "text":  "Some non-empty review text."
                }
                """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testMetrics() = testApplication {
        application { analyzerModule() }

        val response = client.get("/metrics")
        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.bodyAsText()
        assertContains(body, "app_uptime_seconds{service=\"data-analyzer-server\"")
    }

}
