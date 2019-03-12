package com.hlandim.marvelheroes.database.model

import android.arch.persistence.room.*
import com.hlandim.marvelheroes.R
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "hero")
class Hero(
    @PrimaryKey
    @ColumnInfo(name = "hero_id")
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "modified")
    var modified: String,
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

    fun getModifiedString(): String? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault())
        val formatNew = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatNew.format(format.parse(modified))
    }
}