package ch.hslu.cmpproject

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import ch.hslu.cmpproject.cache.WebDatabaseDriverFactory
import ch.hslu.cmpproject.network.SpaceXApi
import ch.hslu.cmpproject.viewmodel.TaskViewModel

@OptIn(ExperimentalComposeUiApi::class)
suspend fun main() {
    ComposeViewport {
        // Datenbank
        val api = SpaceXApi()
        val sdk = SpaceXSDK(
            databaseDriverFactory = WebDatabaseDriverFactory(),
            api = api
        )

        val taskViewModel = TaskViewModel(sdk)
        App(taskViewModel)
    }
}