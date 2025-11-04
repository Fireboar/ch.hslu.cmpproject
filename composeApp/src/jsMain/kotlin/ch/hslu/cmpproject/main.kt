package ch.hslu.cmpproject

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport


@OptIn(ExperimentalComposeUiApi::class)
suspend fun main() {
    ComposeViewport {
        AppRoot()
    }
}