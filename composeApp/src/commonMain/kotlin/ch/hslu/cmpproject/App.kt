package ch.hslu.cmpproject

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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