package ch.hslu.cmpproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ch.hslu.cmpproject.cache.AppDatabase
import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.cache.provideDbDriver
import ch.hslu.cmpproject.network.SpaceXApi
import ch.hslu.cmpproject.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ContextHolder.init(this)

        setContent {
            var taskViewModel by remember { mutableStateOf<TaskViewModel?>(null) }

            // Läuft beim Start — kann suspend-Funktionen aufrufen
            LaunchedEffect(Unit) {
                val driver = provideDbDriver(AppDatabase.Schema)
                val database = Database(driver)
                val api = SpaceXApi()
                val sdk = SpaceXSDK(database, api)

                taskViewModel = TaskViewModel(sdk)
            }

            taskViewModel?.let { tvm ->
                App(tvm)
            } ?: run {
                // Optional: Ladeanzeige
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

    }
}

