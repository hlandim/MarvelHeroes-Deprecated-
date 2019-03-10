package com.hlandim.marvelheroes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.hlandim.marvelheroes.database.dao.FavoriteDao
import com.hlandim.marvelheroes.database.model.FavoriteHero

@Database(version = 1, entities = [FavoriteHero::class])
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE : AppDataBase? = null

        fun getDataBase(context: Context) : AppDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "marvel-db").build()
                INSTANCE = instance
                return  instance
            }
        }
    }
}