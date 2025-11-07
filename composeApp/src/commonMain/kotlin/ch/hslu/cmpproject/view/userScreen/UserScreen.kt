package ch.hslu.cmpproject.view.userScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.hslu.cmpproject.view.ServerSync
import ch.hslu.cmpproject.viewmodel.TaskViewModel

//NEW


@Composable
fun UserScreen(
    taskViewModel: TaskViewModel,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        Button(onClick = { taskViewModel.pullTasks() }) {
            Text("Pull Tasks from Server (Overwrites local)")
        }

        Button(onClick = { taskViewModel.postTasks() }) {
            Text("Post Tasks to Server (Overwrites Server)")
        }

        Button(onClick = { taskViewModel.mergeTasks() }) {
            Text("Merge Tasks (Adds Tasks from both sides)")
        }

    }

    ServerSync(taskViewModel)
}