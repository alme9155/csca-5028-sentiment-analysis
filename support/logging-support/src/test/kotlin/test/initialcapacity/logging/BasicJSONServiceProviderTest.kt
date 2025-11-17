package test.cu.csca5028.alme9155.logging

import cu.csca5028.alme9155.logging.BasicJSONLoggerFactory
import cu.csca5028.alme9155.logging.LogLevel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BasicJSONServiceProviderTest {

    private val originalOut = System.out
    private val output = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        output.reset()
        System.setOut(PrintStream(output, true, Charsets.UTF_8))

        BasicJSONLoggerFactory.setLevel(LogLevel.DEBUG)
        BasicJSONLoggerFactory.getLogger("WARMUP").info("warmup")
        output.reset()
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }

    private fun rawOutput(): String =
        output.toString(Charsets.UTF_8)

    private fun jsonLines(): List<String> =
        rawOutput()
            .lines()
            .map { it.trim() }
            .filter { it.startsWith("{") && it.endsWith("}") }

    @Test
    fun logJSONTest() {
        val logger = BasicJSONLoggerFactory.getLogger("Test")
        logger.info("Hello world")

        val lines = jsonLines()
        val raw = rawOutput()

        assertTrue(lines.isNotEmpty(), "No log output. Raw output was:\n$raw")

        val log = lines.last()

        assertTrue(
            log.contains("\"level\":\"INFO\""),
            "Expected level INFO in log.\nLog line:\n$log"
        )
        assertTrue(
            log.contains("\"logger\":\"Test\""),
            "Expected logger name Test in log.\nLog line:\n$log"
        )
        assertTrue(
            log.contains("\"message\":\"Hello world\""),
            "Expected message 'Hello world' in log.\nLog line:\n$log"
        )
    }

    @Test
    fun logLevelTest() {
        val logger = BasicJSONLoggerFactory.getLogger("Levels")
        logger.debug("debug")
        logger.info("info")
        logger.warn("warn")
        logger.error("error")

        val lines = jsonLines()
        val raw = rawOutput()

        assertTrue(
            lines.size >= 4,
            "Expected at least 4 JSON log lines, got ${lines.size}.\nRaw output was:\n$raw"
        )

        val full = rawOutput()
        val idxDebug = full.indexOf("\"level\":\"DEBUG\"")
        val idxInfo  = full.indexOf("\"level\":\"INFO\"", idxDebug + 1)
        val idxWarn  = full.indexOf("\"level\":\"WARN\"", idxInfo + 1)
        val idxError = full.indexOf("\"level\":\"ERROR\"", idxWarn + 1)

        assertTrue(idxDebug >= 0, "DEBUG level not found in output:\n$full")
        assertTrue(idxInfo  > idxDebug, "INFO should appear after DEBUG. Output:\n$full")
        assertTrue(idxWarn  > idxInfo,  "WARN should appear after INFO. Output:\n$full")
        assertTrue(idxError > idxWarn,  "ERROR should appear after WARN. Output:\n$full")

        // Sanity check on the last 4 JSON lines
        val last4 = lines.takeLast(4)
        val levels = last4.map { line ->
            // crude extraction: ... "level":"DEBUG","logger" ...
            val after = line.substringAfter("\"level\":\"", missingDelimiterValue = "")
            after.substringBefore('"', missingDelimiterValue = "")
        }

        assertEquals(
            listOf("DEBUG", "INFO", "WARN", "ERROR"),
            levels,
            "Last 4 JSON lines did not have expected level sequence.\nLines:\n${last4.joinToString("\n")}"
        )
    }

    @Test
    fun logExceptionTest() {
        val logger = BasicJSONLoggerFactory.getLogger("Error")
        logger.error("Failed", RuntimeException("Boom!"))

        val lines = jsonLines()
        val raw = rawOutput()

        assertTrue(lines.isNotEmpty(), "No log output. Raw output was:\n$raw")

        val log = lines.last()

        assertTrue(
            log.contains("\"message\":\"Failed\""),
            "Expected message 'Failed' in log.\nLog line:\n$log"
        )
        assertTrue(
            log.contains("\"exception\":"),
            "Expected 'exception' field in log.\nLog line:\n$log"
        )
        assertTrue(
            log.contains("RuntimeException"),
            "Expected 'RuntimeException' text in exception.\nLog line:\n$log"
        )
        assertTrue(
            log.contains("Boom!"),
            "Expected 'Boom!' text in exception.\nLog line:\n$log"
        )
    }
}