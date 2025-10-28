package ch.hslu.cmpproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ch.hslu.cmpproject.cache.AndroidDatabaseDriverFactory
import ch.hslu.cmpproject.network.SpaceXApi
import ch.hslu.cmpproject.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Datenbank
        val api = SpaceXApi()
        val sdk = SpaceXSDK(
            databaseDriverFactory = AndroidDatabaseDriverFactory(context = this),
            api = api
        )

        setContent {
            val taskViewModel = TaskViewModel(sdk = sdk)
            App(taskViewModel)
        }
    }
}

