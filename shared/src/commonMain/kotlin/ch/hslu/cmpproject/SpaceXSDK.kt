package ch.hslu.cmpproject

import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.entity.Task
import ch.hslu.cmpproject.network.SpaceXApi


class SpaceXSDK(val database: Database, val api: SpaceXApi) {

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