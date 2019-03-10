package com.hlandim.marvelheroes.database.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.hlandim.marvelheroes.model.Hero


@Entity(tableName = "favorite")
data class FavoriteHero(
    @PrimaryKey(autoGenerate = false)
    var id: Int,

    @Embedded(prefix = "hero_")
    var hero: Hero
)