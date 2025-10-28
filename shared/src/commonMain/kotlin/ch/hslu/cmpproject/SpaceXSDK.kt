package ch.hslu.cmpproject

import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.cache.DatabaseDriverFactory
import ch.hslu.cmpproject.entity.Task
import ch.hslu.cmpproject.network.SpaceXApi


class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory, val api: SpaceXApi) {
    private val database = Database(databaseDriverFactory)

    @Throws(Exception::class)
    suspend fun getTasks(forceReload: Boolean): List<Task> {
        val cachedTasks = database.getAllTasks()
        return if (!forceReload) {
            cachedTasks
        } else {
            api.getAllTasks().also {
                database.clearAndCreateTasks(it)
            }
        }
    }

    suspend fun addTask(task: Task) {
        database.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        database.deleteTask(task)
    }

    suspend fun updateTask(task: Task) {
        database.updateTask(task)
    }


}