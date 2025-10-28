package ch.hslu.cmpproject

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ch.hslu.cmpproject.cache.DesktopDatabaseDriverFactory
import ch.hslu.cmpproject.network.SpaceXApi
import ch.hslu.cmpproject.viewmodel.TaskViewModel


fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Multiplatform Project",
    ) {

        // Datenbank
        val api = SpaceXApi()
        val sdk = SpaceXSDK(
            databaseDriverFactory = DesktopDatabaseDriverFactory(),
            api = api
        )

        val taskViewModel = TaskViewModel(sdk = sdk)
        App(taskViewModel)
    }
}