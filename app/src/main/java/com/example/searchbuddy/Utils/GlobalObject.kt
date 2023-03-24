package com.example.searchbuddy.Utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class GlobalObject : Application() {
    companion object {
        var ctx: Context? = null

        fun applicationContext(): Context
        {
            return ctx!!.applicationContext

        }

        fun getSharedPreferences(context: Context): SharedPreferences
        {
            val fileName = "Storage"
            return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        }
    }


    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
    }
}
