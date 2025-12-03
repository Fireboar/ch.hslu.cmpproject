package ch.hslu.cmpproject.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.cmpproject.TaskSDK
import ch.hslu.cmpproject.entity.Task
import ch.hslu.cmpproject.model.SyncMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel (private val sdk: TaskSDK) : ViewModel(){
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _syncMessage = MutableStateFlow(
        SyncMessage("", isPositive = false)
    )
    val syncMessage: StateFlow<SyncMessage> = _syncMessage

    private var _isServerOnline: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isServerOnline: StateFlow<Boolean> = _isServerOnline

    private var clearMessageJob: kotlinx.coroutines.Job? = null

    init {
        checkServerStatus()
        loadTasks()
    }

    fun setSyncMessage(message: String, positive: Boolean) {
        clearMessageJob?.cancel()
        _syncMessage.value = SyncMessage(message, positive)

        clearMessageJob = viewModelScope.launch {
            delay(8000)
            _syncMessage.value = SyncMessage("", isPositive = false)
        }
    }

    fun checkServerStatus(){
        viewModelScope.launch {
            while (true) {
                isInSync()
                isServerOnline()
                delay(5000)
            }
        }
    }

    fun isInSync(){
        viewModelScope.launch {
            val inSync = sdk.isInSync()
            if (inSync) {
                setSyncMessage("Lokale Daten und Server sind synchron", true)
            } else {
                setSyncMessage("Server nicht synchron oder hat keine Tasks", false)
            }
        }
    }

    fun isServerOnline() {
        viewModelScope.launch {
            _isServerOnline.value = sdk.isServerOnline()
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val loadedTasks = sdk.getTasks()
            _tasks.value = loadedTasks.toList()
            
        }
    }

    fun postTasks() {
        viewModelScope.launch {
            val success = sdk.postAllTasks(isServerOnline.value)
            if(success){
                setSyncMessage("Tasks wurden auf den Server gepostet.", true)
            } else {
                setSyncMessage("Fehler beim Posten der Tasks.", false)
            }
            loadTasks()
            
        }
    }

    fun pullTasks() {
        viewModelScope.launch {
            val success = sdk.pullTasks(isServerOnline.value)
            if(success){
                setSyncMessage("Tasks vom Server geladen und lokal synchronisiert.", true)
            } else {
                setSyncMessage("Fehler beim Laden der Tasks.", false)
            }
            loadTasks()
            
        }
    }

    fun mergeTasks() {
        viewModelScope.launch {
            val success = sdk.mergeTasks(isServerOnline.value)
            if(success){
                setSyncMessage("Server- und lokale Tasks wurden zusammengeführt.", true)
            } else {
                setSyncMessage("Fehler beim Mergen der Tasks.", false)
            }
            loadTasks()
            
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
            val success = sdk.addTask(task, isServerOnline.value)
            if (success) {
                setSyncMessage("'${task.title}' erfolgreich hinzugefügt und synchronisiert.", true)
            } else {
                setSyncMessage("'${task.title}' konnte nicht auf den Server hochgeladen werden.", false)
            }
            loadTasks()
            
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val success = sdk.deleteTask(task, isServerOnline.value)
            if(success){
                setSyncMessage("'${task.title}' erfolgreich gelöscht und synchronisiert.", true)
            } else {
                setSyncMessage("'${task.title}' konnte nicht vom Server gelöscht werden.", false)
            }

            loadTasks()
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch {
            val success = sdk.updateTask(task,isServerOnline.value)
            if(success) {
                setSyncMessage("'${task.title}' erfolgreich aktualisiert.", true)
            } else {
                setSyncMessage("'${task.title}' konnte nicht synchronisiert werden.", false)
            }

            loadTasks()
        }
    }

    fun moveTask(task: Task, newStatus: String) {
        viewModelScope.launch {
            val updatedTask = task.copy(status = newStatus)
            val success = sdk.updateTask(updatedTask,isServerOnline.value)
            if(success) {
                setSyncMessage("'${task.title}' erfolgreich aktualisiert.", true)
            } else {
                setSyncMessage("'${task.title}' konnte nicht synchronisiert werden.", false)
            }

            loadTasks()
        }
    }


}


