package cu.csca5028.alme9155.workflow

interface WorkFinder<T> {
    fun findRequested(name: String): List<T>

    fun markCompleted(info: T)
}