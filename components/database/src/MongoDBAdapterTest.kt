package cu.csca5028.alme9155.database

import com.mongodb.client.AggregateIterable
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import cu.csca5028.alme9155.sentiment.AnalyzeResponse
import io.mockk.*
import org.bson.Document
import org.bson.conversions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class MongoDBAdapterTest {

    private lateinit var mockReviewsCollection: MongoCollection<Document>

    @BeforeEach
    fun setUp() {
        mockkStatic(System::class)
        every { System.currentTimeMillis() } returns 1_000L
        mockReviewsCollection = mockk(relaxed = true)
        injectMongoCollection("reviewsCollection", mockReviewsCollection)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    private fun injectMongoCollection(
        fieldBaseName: String,
        mock: MongoCollection<Document>
    ) {
        val clazz = MongoDBAdapter::class.java
        val field = clazz.declaredFields.firstOrNull {
            it.name.startsWith(fieldBaseName) && it.name.contains("\$delegate")
        } ?: error("Delegate field for $fieldBaseName not found")

        field.isAccessible = true
        val lazyMock: Lazy<MongoCollection<Document>> = lazy { mock }
        field.set(null, lazyMock)
    }

    @Test
    fun testUpsertAnalyzeResult upserts document with normalized title`() {
        val response = AnalyzeResponse(
            title = "  Fight   Club  ",
            text = "sample review",
            labelId = 4,
            labelText = "positive",
            probabilities = mapOf("0" to 0.1, "4" to 0.9)
        )

        val optionsSlot = slot<com.mongodb.client.model.ReplaceOptions>()
        val filterSlot = slot<Document>()

        every {
            mockReviewsCollection.replaceOne(capture(filterSlot), any<Document>(), capture(optionsSlot))
        } returns mockk<UpdateResult>()

        val result = MongoDBAdapter.upsertAnalyzeResult("UI", response)

        assertEquals(1, result)
        val filterDoc = filterSlot.captured
        assertEquals("fight club", filterDoc.getString("normalizedTitle"))
        assertEquals("UI", filterDoc.getString("source"))

        assertTrue(optionsSlot.captured.isUpsert)
    }

    @Test
    fun getTopMovies() {
        val docs = listOf(
            Document(mapOf("title" to "Movie A", "score" to 3.5)),
            Document(mapOf("title" to "Movie B", "score" to 2.0))
        )

        val aggregateIterable = mockk<AggregateIterable<Document>>()
        every { aggregateIterable.iterator() } returns docs.iterator()

        every {
            mockReviewsCollection.aggregate(any<List<Bson>>(), Document::class.java)
        } returns aggregateIterable

        val results = MongoDBAdapter.getTopMovies()

        assertEquals(2, results.size)
        assertEquals("Movie A", results[0].title)
        assertEquals(3.5, results[0].score)
        assertEquals("Movie B", results[1].title)
        assertEquals(2.0, results[1].score)
    }
}
