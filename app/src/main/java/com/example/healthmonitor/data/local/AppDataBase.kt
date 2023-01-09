package com.example.healthmonitor.data.local

import android.content.Context
import com.example.healthmonitor.sqldelight.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver

object AppDatabase {

    private const val DATABASE_NAME = "MyDatabase.db"
    private var INSTANCE: Database? = null

    fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
        INSTANCE ?: Database(
            AndroidSqliteDriver(Database.Schema, context, DATABASE_NAME)
        ).also {
            INSTANCE = it
        }
    }

}