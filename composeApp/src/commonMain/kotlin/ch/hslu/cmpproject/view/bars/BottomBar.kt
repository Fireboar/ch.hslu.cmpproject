package ch.hslu.cmpproject.view.bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import ch.hslu.cmpproject.view.ScreenType

@Composable
fun BottomNavigationBar(
    currentScreen: ScreenType,
    onNavigate: (ScreenType) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF1E88E5)
    ) {
        NavigationBarItem(
            selected = currentScreen == ScreenType.KANBAN,
            onClick = { onNavigate(ScreenType.KANBAN) },
            icon = {
                if (currentScreen == ScreenType.KANBAN) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Kanban")
                } else {
                    Icon(Icons.AutoMirrored.Outlined.List, contentDescription = "Kanban")
                }
            },
            label = { Text("Kanban", fontSize = 14.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.White,
                selectedTextColor = Color.Black,
                unselectedTextColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentScreen == ScreenType.ADDTASK,
            onClick = { onNavigate(ScreenType.ADDTASK) },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Task") },
            label = { Text("Add Task", fontSize = 14.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.White,
                selectedTextColor = Color.Black,
                unselectedTextColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentScreen == ScreenType.USER,
            onClick = { onNavigate(ScreenType.USER) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "User") },
            label = { Text("User", fontSize = 14.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.White,
                selectedTextColor = Color.Black,
                unselectedTextColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}
