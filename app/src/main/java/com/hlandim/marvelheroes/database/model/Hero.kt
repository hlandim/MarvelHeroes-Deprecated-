package com.hlandim.marvelheroes.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.hlandim.marvelheroes.R
import java.io.Serializable

@Entity(tableName = "hero")
class Hero(
    @PrimaryKey
    @ColumnInfo(name = "hero_id")
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @Embedded
    var thumbnail: Thumbnail,
    @ColumnInfo(name = "resourceURI")
    var resourceURI: String,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean,
    @Embedded(prefix = "comics_")
    var comics: ParticipationResponse,
    @Embedded(prefix = "series_")
    var series: ParticipationResponse,
    @Embedded(prefix = "stories_")
    var stories: ParticipationResponse,
    @Embedded(prefix = "events_")
    var events: ParticipationResponse
) : Serializable {

    constructor() :

            this(
                0,
                "",
                Thumbnail(0, "", ""),
                "",
                false,
                ParticipationResponse(0, 0, 0, 0, emptyList()),
                ParticipationResponse(0, 0, 0, 0, emptyList()),
                ParticipationResponse(0, 0, 0, 0, emptyList()),
                ParticipationResponse(0, 0, 0, 0, emptyList())
            )


    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Hero)
            return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id + 30
    }

    fun getFavoriteImage(): Int {
        return if (favorite) {
            R.drawable.ic_star_filled
        } else {
            R.drawable.ic_star
        }
    }
}