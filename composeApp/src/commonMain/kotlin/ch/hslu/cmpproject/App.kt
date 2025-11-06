package ch.hslu.cmpproject

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ch.hslu.cmpproject.cache.AppDatabase
import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.cache.provideDbDriver
import ch.hslu.cmpproject.network.TaskApi
import ch.hslu.cmpproject.view.Navigation
import ch.hslu.cmpproject.viewmodel.TaskViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(taskViewModel: TaskViewModel) {

    MaterialTheme {
        Navigation(taskViewModel = taskViewModel)
    }

}


@Composable
fun AppRoot() {
    var taskViewModel by remember { mutableStateOf<TaskViewModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val driver = provideDbDriver(AppDatabase.Schema)
        val database = Database(driver)
        val api = TaskApi()
        val sdk = TaskSDK(database, api)
        taskViewModel = TaskViewModel(sdk)
        isLoading = false
    }

    if (isLoading) {
        Text("Loading…")
    } else {
        taskViewModel?.let { App(it) }
    }
}