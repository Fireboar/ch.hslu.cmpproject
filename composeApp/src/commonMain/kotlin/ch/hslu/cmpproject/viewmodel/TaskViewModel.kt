package ch.hslu.cmpproject.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.cmpproject.SpaceXSDK
import ch.hslu.cmpproject.entity.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel (private val sdk: SpaceXSDK) : ViewModel(){
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadTasks()
    }

    private fun loadTasks(forceReload: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedTasks = sdk.getTasks(forceReload)
                _tasks.value = loadedTasks.toList()
            } catch (e: Exception) {
                // Fehlerbehandlung hier
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTask(title: String, description: String, dueDate: String, dueTime: String, status:String) {
        val task = Task(
            id = 0,
            title = title,
            description = description,
            dueDate = dueDate,
            dueTime = dueTime,
            status = status
        )
        addTask(task)
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Task zur lokalen Datenbank hinzufügen
                sdk.addTask(task)

                // Flow aktualisieren, sodass die UI sofort reagiert
                val updatedTasks = sdk.getTasks(forceReload = false)
                _tasks.value = updatedTasks
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Task aus der lokalen Datenbank löschen
                sdk.deleteTask(task)

                // Flow aktualisieren, sodass UI reagiert
                _tasks.value = sdk.getTasks(forceReload = false)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun moveTask(task: Task, newStatus: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Task mit neuem Status aktualisieren
                val updatedTask = task.copy(status = newStatus)
                sdk.updateTask(updatedTask)

                // Flow aktualisieren, sodass UI reagiert
                _tasks.value = sdk.getTasks(forceReload = false)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }



}


