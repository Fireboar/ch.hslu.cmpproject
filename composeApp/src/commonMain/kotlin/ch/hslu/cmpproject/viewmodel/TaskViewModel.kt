package ch.hslu.cmpproject.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.cmpproject.TaskSDK
import ch.hslu.cmpproject.entity.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel (private val sdk: TaskSDK) : ViewModel(){
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage

    init {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val inSync = sdk.isInSync()
                if (!inSync) {
                    // Meldung anzeigen, z.B. Snackbar oder StateFlow
                    _syncMessage.value = "Lokale Daten und Server sind nicht synchron!"
                }

                loadTasks()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedTasks = sdk.getTasks()
                _tasks.value = loadedTasks.toList()
            } catch (e: Exception) {
                // Fehlerbehandlung hier
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postAllTasksForce() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                sdk.postAllTasksForce()
                // Flow aktualisieren
                loadTasks()
                _syncMessage.value = "Alle Tasks wurden auf den Server gepostet und synchronisiert."
            } catch (e: Exception) {
                e.printStackTrace()
                _syncMessage.value = "Fehler beim Posten der Tasks: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun pullTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                sdk.pullTasks()
                loadTasks()
                _syncMessage.value = "Tasks vom Server geladen und lokal synchronisiert."
            } catch (e: Exception) {
                e.printStackTrace()
                _syncMessage.value = "Fehler beim Laden der Tasks: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun mergeTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                sdk.mergeTasks()  // ruft die merge-Logik im SDK auf
                loadTasks()       // Flow aktualisieren, sodass UI reagiert
                _syncMessage.value = "Server- und lokale Tasks wurden zusammengeführt und synchronisiert."
            } catch (e: Exception) {
                e.printStackTrace()
                _syncMessage.value = "Fehler beim Mergen der Tasks: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTask(title: String, description: String, dueDate: String, dueTime: String, status:String?) {
        viewModelScope.launch {
            val task = Task(
                id = 0,
                title = title,
                description = description,
                dueDate = dueDate,
                dueTime = dueTime,
                status = status ?: "To Do"
            )
            addTask(task)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                sdk.addTask(task)
                loadTasks()
                _syncMessage.value = "Task '${task.title}' erfolgreich hinzugefügt und synchronisiert."
            } catch (e: Exception) {
                _syncMessage.value = "Fehler beim Hinzufügen des Tasks '${task.title}': ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                sdk.deleteTask(task)
                loadTasks()
                _syncMessage.value = "Task '${task.title}' erfolgreich gelöscht und synchronisiert."
            } catch (e: Exception) {
                e.printStackTrace()
                _syncMessage.value = "Fehler beim Löschen der Task '${task.title}': ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun moveTask(task: Task, newStatus: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val updatedTask = task.copy(status = newStatus)
            try {
                sdk.updateTask(updatedTask)
                _syncMessage.value = "Task '${task.title}' erfolgreich aktualisiert."
            } catch (e: Exception) {
                _syncMessage.value = "Server update failed '${e.message}'"
            } finally {
                loadTasks() // Flow/UI aktualisieren
                _isLoading.value = false
            }
        }
    }



}


