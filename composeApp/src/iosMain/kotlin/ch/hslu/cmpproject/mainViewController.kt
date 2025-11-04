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
    AppRoot()
}