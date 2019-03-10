package com.hlandim.marvelheroes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.hlandim.marvelheroes.database.dao.FavoriteDao
import com.hlandim.marvelheroes.database.model.FavoriteHero

@Database(version = 1, entities = [FavoriteHero::class])
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}