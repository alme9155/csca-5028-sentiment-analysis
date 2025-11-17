package cu.csca5028.alme9155.logging

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import cu.csca5028.alme9155.logging.LogLevel

@Serializable
data class LogEntry(
    val timestamp: String,
    val level: String,
    val logger: String,
    val thread: Long,
    val message: String,
    val exception: String? = null
)

class BasicJSONLogger(private val name: String) {
    private val json = Json { prettyPrint = false; ignoreUnknownKeys = true }
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneId.of("UTC"))

    fun debug(msg: String) = log(LogLevel.DEBUG, "DEBUG", msg)
    fun info(msg: String) = log(LogLevel.INFO, "INFO", msg)
    fun warn(msg: String) = log(LogLevel.WARN, "WARN", msg)
    fun error(msg: String, throwable: Throwable? = null) = log(LogLevel.ERROR, "ERROR", msg, throwable)

    private fun log(level: LogLevel, levelStr: String, msg: String, throwable: Throwable? = null) {
        // This is the only check that respects the global level
        if (!BasicJSONLoggerFactory.isEnabled(level)) return

        val timestamp = formatter.format(Instant.now())
        val threadId = Thread.currentThread().id
        val logEntry = LogEntry(
            timestamp = timestamp,
            level = levelStr,
            logger = name,
            thread = threadId,
            message = msg,
            exception = throwable?.let { serializeException(it) }
        )
        val jsonString = json.encodeToString(logEntry)
        synchronized(System.out) {
            System.out.println(jsonString)
        }
    }

    private fun serializeException(t: Throwable): String {
        val sw = StringWriter()
        t.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}