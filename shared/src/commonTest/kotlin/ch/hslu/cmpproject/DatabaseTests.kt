package ch.hslu.cmpproject


import ch.hslu.cmpproject.cache.AppDatabase
import ch.hslu.cmpproject.cache.Database
import ch.hslu.cmpproject.cache.provideDbDriver
import ch.hslu.cmpproject.entity.Task
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals


/*
class DatabaseTests {


    @Test
    fun testTaskPersistence() = runTest {

        val driver = provideDbDriver(AppDatabase.Schema)
        val db = Database(driver)
        db.clearAndCreateTasks(emptyList())

        val task = Task(0, "Test", "Beschreibung", "2025-10-30", "12:00", "To Do")
        db.insertTask(task)

        val tasksBefore = db.getAllTasks()
        assertEquals(1, tasksBefore.size)
    }
}*/
