package cu.csca5028.alme9155.logging

import java.util.concurrent.ConcurrentHashMap
import cu.csca5028.alme9155.logging.LogLevel

object BasicJSONLoggerFactory {
    @Volatile
    private var currentLevel: LogLevel = LogLevel.INFO

    private val loggers = ConcurrentHashMap<String, BasicJSONLogger>()

    fun setLevel(level: LogLevel) {
        currentLevel = level
    }

    fun getLogger(name: String): BasicJSONLogger =
        loggers.getOrPut(name) { BasicJSONLogger(name) }

    internal fun isEnabled(level: LogLevel): Boolean =
        level >= currentLevel
}
