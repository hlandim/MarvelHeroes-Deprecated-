package com.hlandim.marvelheroes

import android.app.Application
import android.arch.persistence.room.Room
import com.hlandim.marvelheroes.database.AppDataBase

class MyApplication : Application() {

    companion object {
        var database: AppDataBase? = null
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDataBase::class.java, "marvel-db").build()
    }
}