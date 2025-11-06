package ch.hslu.cmpproject

import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.entity.Task
import ch.hslu.cmpproject.network.TaskApi


class TaskSDK(val database: Database, val api: TaskApi) {

    suspend fun isInSync(): Boolean {
        return try {
            val serverTasks = api.getAllTasks()
            val localTasks = database.getAllTasks()

            // Vergleich nach ID und Inhalt
            val serverSet = serverTasks.map { it.id to it }.toSet()
            val localSet = localTasks.map { it.id to it }.toSet()

            serverSet == localSet
        } catch (e: Exception) {
            false // Wenn Server nicht erreichbar, als nicht synchron betrachten
        }
    }

    suspend fun postAllTasksForce() {
        try {
            // Alle lokalen Tasks auf den Server „ersetzen“ (upsert)
            val localTasks = database.getAllTasks()
            api.replaceTasks(localTasks)

            // Optional: Lokale DB aktualisieren, falls IDs vom Server abweichen
            val syncedTasks = api.getAllTasks()
            database.replaceTasks(syncedTasks)

        } catch (e: Exception) {
            // Fehlerbehandlung
            throw e
        }
    }

    suspend fun pullTasks() {
        try {
            // Alle Tasks vom Server holen
            val serverTasks = api.getAllTasks()
            // Lokale DB überschreiben
            database.replaceTasks(serverTasks)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun mergeTasks() {
        try {
            // Lokale und Server-Tasks laden
            val serverTasks = api.getAllTasks()
            val localTasks = database.getAllTasks()

            // Kombiniere Tasks, lokale ID als Maßstab
            val mergedTasks = (serverTasks + localTasks)
                .distinctBy { it.id } // Duplikate vermeiden

            // Auf Server in einem Schritt ersetzen
            api.replaceTasks(mergedTasks)

            // Danach lokale DB ersetzen, um IDs zu synchronisieren
            val updatedServerTasks = api.getAllTasks()
            database.replaceTasks(updatedServerTasks)

        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getTasks(): List<Task> {
        return database.getAllTasks()
    }

    suspend fun addTask(task: Task) {
        //Lokal einfügen
        val newTask = database.insertTask(task)

        println("Task lokal eingefügt: id=${newTask.id}, title=${newTask.title}, description=${newTask.description}, dueDate=${newTask.dueDate}, dueTime=${newTask.dueTime}, status=${newTask.status}")

        //Auf Server versuchen
        try {
            api.addTask(newTask)
        } catch (e: Exception) {
            println("Server add failed for task id=${newTask.id}: ${e.message}")
            throw Exception("Server add failed for task id=${newTask.id}: ${e.message}", e)
        }
    }

    suspend fun updateTask(task: Task) {
        // 1️⃣ Lokales Update sofort, unabhängig vom Server
        database.updateTask(task)

        try {
            // 2️⃣ Versuche Server zu aktualisieren
            api.updateTask(task) // kann Exception werfen
        } catch (e: Exception) {
            // 3️⃣ Fehler nur loggen oder weiterwerfen, lokales Update ist bereits erfolgt
            throw Exception("'${e.message}'", )
        }
    }

    suspend fun deleteTask(task: Task) {
        database.deleteTask(task)
        try {
            api.deleteTask(task.id.toLong()) // Server löschen
        } catch (e: Exception) {
            database.deleteTask(task)        // lokal trotzdem löschen
            throw e
        }
    }
}