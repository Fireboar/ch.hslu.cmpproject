package ch.hslu.cmpproject.network

import ch.hslu.cmpproject.entity.Task
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TaskApi() {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getAllTasks(): List<Task> {
        return httpClient.get("http://192.168.1.22:8080/tasks").body()
    }

    suspend fun addTask(task: Task): Task? {
        return try {
            val response = httpClient.post("http://192.168.1.22:8080/tasks") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }

            if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {
                response.body<Task>()
            } else {
                throw Exception("Server returned ${response.status} for task id=${task.id}")
            }
        } catch (e: Exception) {
            throw Exception("${e.message}")
        }
    }

    suspend fun deleteTask(id: Long) {
        httpClient.delete("http://192.168.1.22:8080/tasks/$id")
    }

    suspend fun updateTask(task: Task): Task? {
        return try {
            val response = httpClient.put("http://192.168.1.22:8080/tasks/${task.id}") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }

            if (response.status == HttpStatusCode.OK) {
                response.body<Task>() // jetzt sicher
            } else {
                // Optional: Loggen oder Exception werfen
                throw Exception("Server returned ${response.status} for task ${task.id}")
            }
        } catch (e: Exception) {
            throw Exception("${e.message}")
        }
    }

    suspend fun replaceTasks(tasks: List<Task>) {
        httpClient.post("http://192.168.1.22:8080/tasks/replace") {
            contentType(ContentType.Application.Json)
            setBody(tasks)
        }
    }
}