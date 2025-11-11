package ch.hslu.cmpproject.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ch.hslu.cmpproject.PlatformType
import ch.hslu.cmpproject.getPlatform
import ch.hslu.cmpproject.view.addTaskScreen.AddTaskScreen
import ch.hslu.cmpproject.view.addTaskScreen.AddTaskScreenDesktopWeb
import ch.hslu.cmpproject.view.bars.BottomNavigationBar
import ch.hslu.cmpproject.view.bars.ServerSync
import ch.hslu.cmpproject.view.bars.TopBar
import ch.hslu.cmpproject.view.kanbanScreen.KanbanScreen
import ch.hslu.cmpproject.view.userScreen.UserScreen
import ch.hslu.cmpproject.viewmodel.TaskViewModel


// Screens definieren
enum class ScreenType { KANBAN, ADDTASK, USER}

@Composable
fun Navigation(taskViewModel: TaskViewModel) {
    var currentScreen by rememberSaveable {
        mutableStateOf(ScreenType.KANBAN)
    }


    fun navigateTo(screen: ScreenType, taskId: Int? = null) {
        currentScreen = screen
    }


    Scaffold(
        topBar = {
            val screenTitle = currentScreen.toString()
                .lowercase().replaceFirstChar { it.uppercaseChar() }

            TopBar(screenTitle)
        },
        bottomBar = {
            Column (Modifier.fillMaxWidth()) {

                ServerSync(taskViewModel)

                BottomNavigationBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> navigateTo(screen) }
                )
            }
        }
    ) { paddingValues ->
        //Content
        when (currentScreen) {

            ScreenType.KANBAN -> KanbanScreen(taskViewModel, paddingValues)

            ScreenType.ADDTASK -> {
                when (getPlatform()) {
                    PlatformType.DESKTOP, PlatformType.WEB -> AddTaskScreenDesktopWeb(
                        taskViewModel,
                        paddingValues
                    )
                    else -> AddTaskScreen(taskViewModel, paddingValues)
                }
            }

            ScreenType.USER -> UserScreen(taskViewModel,paddingValues)
        }

    }
}