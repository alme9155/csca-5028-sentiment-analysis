package cu.csca5028.alme9155.workflow

import org.slf4j.LoggerFactory

class NoopWorkFinder : WorkFinder<NoopTask> {

    override fun findRequested(name: String): List<NoopTask> {
        LoggerFactory.getLogger(NoopWorkFinder::class.java)
            .info("finding work.")
        return mutableListOf(NoopTask("task-name", "task-value"))
    }

    override fun markCompleted(info: NoopTask) {
        LoggerFactory.getLogger(NoopWorkFinder::class.java)
            .info("marking work complete.")
    }
}
