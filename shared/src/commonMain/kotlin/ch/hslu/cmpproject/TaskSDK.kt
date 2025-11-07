package ch.hslu.cmpproject

import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.entity.Task
import ch.hslu.cmpproject.network.TaskApi


class TaskSDK(val database: Database, val api: TaskApi) {

    suspend fun isServerOnline():Boolean{
        return api.isServerOnline()
    }

    suspend fun isInSync(): Boolean {
        val serverTasks = api.getTasks()
        if(serverTasks.isEmpty()){
            return false
        }
        val localTasks = database.getTasks()

        val serverSet = serverTasks.map { it.id to it }.toSet()
        val localSet = localTasks.map { it.id to it }.toSet()

        return serverSet == localSet
    }

    suspend fun postAllTasks(isServerOnline: Boolean): Boolean {
        if (isServerOnline){
            return api.replaceTasks(database.getTasks())
        }
        return false
    }

    suspend fun pullTasks(isServerOnline: Boolean): Boolean {
        if (isServerOnline){
            val serverTasks = api.getTasks()
            if(serverTasks.isNotEmpty()){
                database.replaceTasks(serverTasks)
                return true
            } else {
                return false
            }
        }
        return false
    }

    suspend fun mergeTasks(isServerOnline: Boolean): Boolean {
        if(isServerOnline){
            val serverTasks = api.getTasks()
            val localTasks = database.getTasks()

            val mergedTasks = (serverTasks + localTasks)
                .distinctBy { it.id }

            val updatedServerTasks = api.getTasks()
            database.replaceTasks(updatedServerTasks)

            return api.replaceTasks(mergedTasks)
        }
        return false
    }

    suspend fun getTasks(): List<Task> {
        return database.getTasks()
    }

    suspend fun addTask(task: Task, isServerOnline: Boolean): Boolean {
        val newTask = database.insertTask(task)
        if (isServerOnline){
            return api.addTask(newTask)
        }
        return false
    }

    suspend fun updateTask(task: Task, isServerOnline: Boolean): Boolean {
        database.updateTask(task)
        if(isServerOnline){
            return api.updateTask(task)
        }
        return false
    }

    suspend fun deleteTask(task: Task, isServerOnline: Boolean): Boolean {
        database.deleteTask(task)
        if(isServerOnline){
            return api.deleteTask(task.id.toLong())
        }
        return false
    }
}