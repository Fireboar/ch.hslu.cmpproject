package ch.hslu.cmpproject

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ch.hslu.cmpproject.cache.AppDatabase
import ch.hslu.cmpproject.entity.Task
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

const val SERVER_PORT = 9090

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

suspend fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }

    // ✅ SQLDelight initialisieren
    val driver = JdbcSqliteDriver("jdbc:sqlite:server.db")

    AppDatabase.Schema.create(driver).await()

    val database = AppDatabase(driver)
    val queries = database.appDatabaseQueries


    // --- Beispiel-Task nur einmal anlegen ---
    if (queries.selectAllTasks().executeAsList().isEmpty()) {
        queries.insertTask(
            title = "Server-Task",
            description = "Dies ist ein Default-Task",
            dueDate = "2025-11-05",
            dueTime = "12:00",
            status = "To Do"
        )
    }

    routing {

        post("/tasks/replace") {
            val tasks = call.receive<List<Task>>()

            // 1. Alle Tasks löschen
            queries.deleteAllTasks()

            // 2. Neue Tasks einfügen
            tasks.forEach { task ->
                queries.insertOrReplaceTask(task.id.toLong(),task.title, task.description, task.dueDate, task.dueTime, task.status)
            }

            call.respond(HttpStatusCode.OK, mapOf("message" to "Tasks replaced successfully"))
        }

        // CREATE
        post("/tasks") {
            val task = call.receive<Task>()

            println("Server received task: id=${task.id}, title=${task.title}, description=${task.description}, dueDate=${task.dueDate}, dueTime=${task.dueTime}, status=${task.status}")

            // Insert oder Update
            queries.insertOrReplaceTask(
                id = task.id.toLong(),
                title = task.title.toString(),
                description = task.description ?: "",
                dueDate = task.dueDate.toString(),
                dueTime = task.dueTime.toString(),
                status = task.status ?: "To Do"
            )

            // Nur Erfolgsstatus zurückgeben
            call.respond(HttpStatusCode.OK, mapOf("message" to "Task upserted successfully"))
        }

        // READ ALL
        get("/tasks") {
            val tasks = queries.selectAllTasks().executeAsList().map { t ->
                Task(
                    id = t.id.toInt(),
                    title = t.title,
                    description = t.description,
                    dueDate = t.dueDate,
                    dueTime = t.dueTime,
                    status = t.status
                )
            }
            call.respond(tasks)
        }

        // READ SINGLE
        get("/tasks/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val task = queries.selectTaskById(id).executeAsOneOrNull()
            if (task == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
                return@get
            }

            call.respond(
                Task(
                    id = task.id.toInt(),
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate,
                    dueTime = task.dueTime,
                    status = task.status
                )
            )
        }

        // UPDATE
        put("/tasks/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val updatedTaskData = call.receive<Task>()

            // Prüfen, ob der Task existiert
            val existing = queries.selectTaskById(id).executeAsOneOrNull()
            if (existing == null) {
                call.respond(HttpStatusCode.NotFound, "Task with id=$id not found")
                return@put
            }

            // Update durchführen
            queries.updateTask(
                title = updatedTaskData.title,
                description = updatedTaskData.description,
                dueDate = updatedTaskData.dueDate,
                dueTime = updatedTaskData.dueTime,
                status = updatedTaskData.status,
                id = id
            )

            // Aktualisierten Task abrufen
            val refreshed = queries.selectTaskById(id).executeAsOneOrNull()
            if (refreshed == null) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to retrieve task after update")
                return@put
            }

            call.respond(
                Task(
                    id = refreshed.id.toInt(),
                    title = refreshed.title,
                    description = refreshed.description,
                    dueDate = refreshed.dueDate,
                    dueTime = refreshed.dueTime,
                    status = refreshed.status
                )
            )
        }

        // DELETE
        delete("/tasks/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val existing = queries.selectTaskById(id).executeAsOneOrNull()
            if (existing == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
                return@delete
            }

            queries.deleteTaskById(id)
            call.respond(HttpStatusCode.OK, mapOf("message" to "Deleted task $id"))
        }
    }
}