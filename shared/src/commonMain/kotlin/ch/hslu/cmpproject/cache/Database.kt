package ch.hslu.cmpproject.cache

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.db.SqlDriver
import ch.hslu.cmpproject.entity.Task

class Database (val driver: SqlDriver){

    private val database = AppDatabase(driver)
    private val dbQuery get() = database.appDatabaseQueries

    internal suspend fun getAllTasks(): List<Task> {
        return dbQuery.selectAllTasks(::mapTaskSelecting).awaitAsList()
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
            description = description ?: "",
            dueDate = dueDate,
            dueTime = dueTime,
            status = status ?: "To Do"
        )
    }

    internal suspend fun replaceTasks(tasks: List<Task>) {
        dbQuery.transaction {
            dbQuery.deleteAllTasks()
            tasks.forEach { task ->
                dbQuery.insertOrReplaceTask(
                    id = task.id.toLong(),
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate,
                    dueTime = task.dueTime,
                    status = task.status
                )
            }
        }
    }

    internal suspend fun insertTask(task: Task): Task {
        dbQuery.insertTask(
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            dueTime = task.dueTime,
            status = task.status
        )
        // Task aus der DB holen
        val newId = dbQuery.lastInsertRowId().executeAsOne()
        val inserted = dbQuery.selectTaskById(newId).executeAsOne()

        // In serialisierbares Task-Modell umwandeln
        return Task(
            id = inserted.id.toInt(),
            title = inserted.title,
            description = inserted.description,
            dueDate = inserted.dueDate,
            dueTime = inserted.dueTime,
            status = inserted.status
        )
    }

    internal suspend fun deleteTask(task: Task) {
        dbQuery.deleteTaskById(task.id.toLong())
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
