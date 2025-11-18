package cu.csca5028.alme9155.sentiment

import kotlin.random.Random
import kotlinx.serialization.Serializable

// Data class for NLP Sentiment Analysis
@Serializable
data class AnalyzeRequest(
    val text: String
)

// Stable response type used by all services
@Serializable
data class AnalyzeResponse(
    val text: String,
    val labelId: Int,
    val labelText: String,
    val probabilities: Map<String, Double>
)

// Rating Labels
val SENTIMENT_LABELS = listOf(
    "very negative",
    "negative",
    "neutral",
    "positive",
    "very positive"
)


/**
 * Fake sentiment model to build UI service before applying real pre-trained model.
 * Returns an AnalyzeResponse.
 */
class FakeSentimentModel(
    private val labels: List<String> = SENTIMENT_LABELS
) {

    /**
     * Static model
     */
    fun predictSentiment(text: String): AnalyzeResponse {
        val raw = labels.map { Random.nextDouble(0.01, 1.0) }
        val sum = raw.sum()

        val probs = raw.map { it / sum }

        val maxIndex = probs.indices.maxByOrNull { probs[it] } ?: 0

        val probabilities = labels.indices.associate { i ->
            labels[i] to probs[i]
        }

        return AnalyzeResponse(
            text = text,
            labelId = maxIndex,
            labelText = labels[maxIndex],
            probabilities = probabilities
        )
    }
}
