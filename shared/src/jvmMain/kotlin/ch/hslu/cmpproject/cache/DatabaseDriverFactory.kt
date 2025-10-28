package ch.hslu.cmpproject.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

class DesktopDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:task.db")
        try {
            AppDatabase.Schema.create(driver)
        } catch (_: Exception) {
            // Tabelle existiert schon -> ignorieren
        }
        return driver
    }
}