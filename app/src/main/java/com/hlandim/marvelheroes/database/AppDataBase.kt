package com.hlandim.marvelheroes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.hlandim.marvelheroes.database.dao.FavoriteDao
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.database.model.Participation
import com.hlandim.marvelheroes.database.model.ParticipationResponse
import com.hlandim.marvelheroes.database.model.Thumbnail

@Database(
    exportSchema = false,
    version = 1, entities = [
        Participation::class,
        ParticipationResponse::class,
        Thumbnail::class,
        Hero::class]
)
@TypeConverters(Converter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "marvel-db").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}