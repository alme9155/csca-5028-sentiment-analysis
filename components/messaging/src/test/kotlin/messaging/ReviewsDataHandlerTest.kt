package cu.csca5028.alme9155.messaging

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import cu.csca5028.alme9155.database.MongoDBAdapter
import cu.csca5028.alme9155.database.RawMovieReview
import io.mockk.*
import org.bson.Document
import org.junit.jupiter.api.*

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class ReviewsDataHandlerTest {

    private lateinit var mockChannel: Channel
    private lateinit var mockConnection: Connection

    @BeforeEach
    fun setUp() {
        // Mock mock object.
        mockkObject(MongoDBAdapter)
        mockkConstructor(ConnectionFactory::class)
        mockConnection = mockk(relaxed = true)
        mockChannel = mockk(relaxed = true)

        every { anyConstructed<ConnectionFactory>().newConnection() } returns mockConnection
        every { mockConnection.createChannel() } returns mockChannel
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun publishAllRawReviews_basicHappyPath() {
        // A long-enough review so it passes the ULTRA STRICT FILTER
        val reviewText =
            "This is a long movie review text longer than fifty characters to pass the filter. love this movie. hate this move. blah blah..."

        val reviewDoc = Document(mapOf("description" to reviewText))
        val reviewsBySource = Document(mapOf("rt" to listOf(reviewDoc)))

        val movieDoc = Document()
        movieDoc["movie_id"] = "m1"
        movieDoc["title"] = "Fight Club"
        movieDoc["year"] = "1999"
        movieDoc["reviews"] = reviewsBySource

        val rawMovie = RawMovieReview(movieId = "m1", raw = movieDoc)
        every { MongoDBAdapter.getAllRawMovieReviews() } returns listOf(rawMovie)

        val bodySlot = slot<ByteArray>()
        every { mockChannel.basicPublish(any(), any(), any(), capture(bodySlot)) } just Runs
        val stats = ReviewsDataHandler.publishAllRawReviews()

        assertEquals(1, stats.totalRawReviews)
        assertEquals(1, stats.publishedCount)
        verify(exactly = 1) {
            mockChannel.basicPublish(any(), any(), any(), any())
        }

        val payload = String(bodySlot.captured)
        assertTrue(payload.contains("Fight Club"))
        assertTrue(payload.contains("long movie review text"))
    }
}
