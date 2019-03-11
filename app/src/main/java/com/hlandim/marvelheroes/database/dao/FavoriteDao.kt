package com.hlandim.marvelheroes.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.hlandim.marvelheroes.database.model.Hero

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM hero ORDER BY name ASC")
    fun getAllFavoriteHeroes(): LiveData<List<Hero>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteHero(vararg heroes: Hero)

    @Delete
    fun removerFavoriteHero(hero: Hero)
}