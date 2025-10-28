package ch.hslu.cmpproject.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToDoScreen(){
    Column (Modifier.padding(top = 32.dp)){
        Text("Hallo Welt", style = MaterialTheme.typography.bodyLarge)
        Text("Hallo Welt",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }

}

