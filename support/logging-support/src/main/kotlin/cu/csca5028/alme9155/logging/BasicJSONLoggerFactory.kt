package cu.csca5028.alme9155.logging


import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap


class BasicJSONLoggerFactory : ILoggerFactory {
    companion object {
        private var loggerMap = ConcurrentHashMap<String, Logger>();
    }

    override fun getLogger(name: String): Logger {
        return loggerMap.computeIfAbsent(name) {
            BasicJSONLogger(it)
        }
    }
}
