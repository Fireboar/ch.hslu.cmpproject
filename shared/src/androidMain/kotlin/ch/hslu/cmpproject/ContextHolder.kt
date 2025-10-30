package ch.hslu.cmpproject

import android.content.Context

object ContextHolder {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}