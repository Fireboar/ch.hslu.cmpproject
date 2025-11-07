package ch.hslu.cmpproject.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ch.hslu.cmpproject.viewmodel.TaskViewModel

@Composable
fun ServerSync (taskViewModel: TaskViewModel) {
    val syncMessage by taskViewModel.syncMessage.collectAsState()
    val serverOnline by taskViewModel.isServerOnline.collectAsState()


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
    ) {
        // Sync-Message
        if (syncMessage.text.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        color = if (syncMessage.isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = syncMessage.text,
                    color = Color.White,
                    softWrap = true
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        // Serverstatus
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth() // Box so breit wie der Text
                .background(
                    color = if (serverOnline) Color(0xFF4CAF50) else Color(0xFFF44336),
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (serverOnline) "Server online" else "Server offline",
                color = Color.White
            )
        }
    }
}