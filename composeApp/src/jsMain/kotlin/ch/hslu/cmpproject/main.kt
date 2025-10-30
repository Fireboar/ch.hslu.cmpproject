package ch.hslu.cmpproject

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import ch.hslu.cmpproject.cache.AppDatabase
import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.cache.provideDbDriver
import ch.hslu.cmpproject.network.SpaceXApi
import ch.hslu.cmpproject.viewmodel.TaskViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text


@OptIn(ExperimentalComposeUiApi::class)
suspend fun main() {
    ComposeViewport {
        var taskViewModel by remember { mutableStateOf<TaskViewModel?>(null) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            val driver = provideDbDriver(AppDatabase.Schema)
            val database = Database(driver)
            val api = SpaceXApi()
            val sdk = SpaceXSDK(database, api)
            taskViewModel = TaskViewModel(sdk)
            isLoading = false
        }

        if (isLoading) {
            // Zeige Ladebildschirm
            Text("Loading…")
        } else {
            taskViewModel?.let { App(it) }
        }
    }
}