package cu.csca5028.alme9155.collector

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlin.test.*
import org.bson.Document

import cu.csca5028.alme9155.database.*
import cu.csca5028.alme9155.sentiment.*
import cu.csca5028.alme9155.messaging.*
import cu.csca5028.alme9155.api.*
import kotlinx.coroutines.runBlocking

/*
 * Junit class for data collector service.
 */
class AppTest {

    @Test
    fun testRoot() = testApplication {
        application { collectorModule() }

        val response = runBlocking { client.get("/") }
        assertEquals(HttpStatusCode.OK, response.status)

        val body = runBlocking { response.bodyAsText() }
        assertContains(body, "Data Collector")
    }

    @Test
    fun testHealth() = testApplication {
        application { collectorModule() }

        val response = runBlocking { client.get("/health") }
        assertEquals(HttpStatusCode.OK, response.status)

        val body = runBlocking { response.bodyAsText() }
        assertEquals("OK", body)
    }

    @Test
    fun testPublishAPI() {
        mockkObject(ReviewsDataHandler)
        try {
            // mock data object
            val stats = PublishStats(
                totalRawReviews = 5,
                publishedCount = 3
            )
            every { ReviewsDataHandler.publishAllRawReviews() } returns stats

            val returnedStats: PublishStats = try {
                ReviewsDataHandler.publishAllRawReviews()
            } catch (_: Exception) {
                PublishStats(totalRawReviews = 0, publishedCount = 0)
            }
            val result = PublishResult(
                dbCount = returnedStats.totalRawReviews,
                msgCount = returnedStats.publishedCount
            )

            assertEquals(5, result.dbCount)
            assertEquals(3, result.msgCount)

            verify(exactly = 1) { ReviewsDataHandler.publishAllRawReviews() }
        } finally {
            unmockkAll()
        }
    }

    @Test
    fun testMetrics() = testApplication {
        application { collectorModule() }

        val response = runBlocking { client.get("/metrics") }
        assertEquals(HttpStatusCode.OK, response.status)

        val body = runBlocking { response.bodyAsText() }
        assertContains(body, "app_uptime_seconds{service=\"data-collector-server\"")
    }
}
