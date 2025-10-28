package ch.hslu.cmpproject.cache

import ch.hslu.cmpproject.entity.Task

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllTasks(): List<Task> {
        return dbQuery.selectAllTasksInfo(::mapTaskSelecting).executeAsList()
    }

    internal fun mapTaskSelecting(
        id: Long,
        title: String,
        description: String?,
        dueDate: String,
        dueTime: String,
        status: String?
    ): Task {
        return Task(
            id = id.toInt(),
            title = title,
            description = description?:"",
            dueDate = dueDate,
            dueTime = dueTime,
            status = status ?: "To Do"
        )
    }

    internal suspend fun clearAndCreateTasks(tasks: List<Task>) {
        dbQuery.transaction {
            dbQuery.removeAllTasks()
            tasks.forEach { task ->
                dbQuery.insertTask(
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate,
                    dueTime = task.dueTime,
                    status = task.status
                )
            }
        }
    }
    internal suspend fun insertTask(task: Task) {
        dbQuery.insertTask(
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            dueTime = task.dueTime,
            status = task.status
        )
    }

    internal suspend fun deleteTask(task: Task) {
        dbQuery.deleteTask(task.id.toLong())
    }

    internal suspend fun updateTask(task: Task) {
        dbQuery.updateTask(
            id = task.id.toLong(),
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            dueTime = task.dueTime,
            status = task.status
        )
    }


}