package ch.hslu.cmpproject

import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import ch.hslu.cmpproject.cache.AppDatabase
import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.cache.provideDbDriver
import ch.hslu.cmpproject.network.SpaceXApi
import ch.hslu.cmpproject.viewmodel.TaskViewModel

@Suppress("unused")
fun mainViewController() = ComposeUIViewController {
    var taskViewModel by remember { mutableStateOf<TaskViewModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Läuft einmal beim Start
    LaunchedEffect(Unit) {
        val driver = provideDbDriver(AppDatabase.Schema)
        val database = Database(driver)
        val api = SpaceXApi()
        val sdk = SpaceXSDK(database, api)
        taskViewModel = TaskViewModel(sdk)
        isLoading = false
    }

    // App nur rendern, wenn TaskViewModel bereit ist
    if (isLoading) {
        Text("Loading…")
    } else {
        taskViewModel?.let { App(it) }
    }
}