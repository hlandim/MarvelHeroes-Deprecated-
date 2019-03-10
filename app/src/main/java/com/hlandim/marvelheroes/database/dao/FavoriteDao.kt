package com.hlandim.marvelheroes.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.hlandim.marvelheroes.database.model.FavoriteHero

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAllFavoriteHeroes(): LiveData<List<FavoriteHero>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteHero(vararg heroes: FavoriteHero)

    @Delete
    fun removerFavoriteHero(hero: FavoriteHero)
}