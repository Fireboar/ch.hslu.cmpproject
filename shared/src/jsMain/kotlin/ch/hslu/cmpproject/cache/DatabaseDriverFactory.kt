package ch.hslu.cmpproject.cache


import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

class WebDatabaseDriverFactory : DatabaseDriverFactory {   // <- Interface implementieren
    override fun createDriver(): SqlDriver {
        // Worker für SQL.js laden
        val worker = Worker(
            js("""new URL('@cashapp/sqldelight-sqljs-worker/sqljs.worker.js', import.meta.url)""")
        )

        // WebWorkerDriver erzeugen
        val driver = WebWorkerDriver(worker)

        // Schema asynchron erstellen
        AppDatabase.Schema.awaitCreate(driver)

        return driver
    }
}