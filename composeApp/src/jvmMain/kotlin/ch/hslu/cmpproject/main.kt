package ch.hslu.cmpproject

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ch.hslu.cmpproject.cache.AppDatabase
import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.cache.provideDbDriver
import ch.hslu.cmpproject.network.SpaceXApi
import ch.hslu.cmpproject.viewmodel.TaskViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Multiplatform Project",
    ) {
        var taskViewModel by remember { mutableStateOf<TaskViewModel?>(null) }

        // Coroutine zum Laden von DB & SDK
        LaunchedEffect(Unit) {
            val driver = provideDbDriver(AppDatabase.Schema)
            val database = Database(driver)
            val api = SpaceXApi()
            val sdk = SpaceXSDK(database, api)

            taskViewModel = TaskViewModel(sdk)
        }

        // App nur rendern, wenn TaskViewModel bereit ist
        taskViewModel?.let { tvm ->
            //tvm.addTask("Beispiel", "W","12.12.1212","12:12",null)
            App(tvm)
        }
    }
}