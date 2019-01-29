package com.`is`.daviondk.imagesearchapp

import com.`is`.daviondk.imagesearchapp.AppDatabase
import android.arch.persistence.room.Room
import android.app.Application

class App : Application() {

    private var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
                .build()
    }

    companion object {

        lateinit var instance: App
    }

    fun getDatabase(): AppDatabase? {
        return database
    }
}