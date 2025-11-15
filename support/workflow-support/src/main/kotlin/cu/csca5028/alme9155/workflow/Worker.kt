package cu.csca5028.alme9155.workflow

interface Worker<T> {
    val name: String
    fun execute(task: T)
}