package com.hlandim.marvelheroes.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.hlandim.marvelheroes.R
import java.io.Serializable
import java.util.*

@Entity
class Hero(
    @PrimaryKey
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @Ignore
    val description: String,
    @Ignore
    val modified: Date,
    @Ignore
    val thumbnail: Thumbnail,
    @Ignore
    val resourceURI: String,
    @Ignore
    var favorite: Boolean,
    @Ignore
    val comics: ParticipationResponse,
    @Ignore
    val series: ParticipationResponse,
    @Ignore
    val stories: ParticipationResponse,
    @Ignore
    val events: ParticipationResponse
) : Serializable {

    constructor() :

            this(
                0,
                "",
                "",
                Date(),
                Thumbnail("", ""),
                "",
                false,
                ParticipationResponse(0, 0, emptyList()),
                ParticipationResponse(0, 0, emptyList()),
                ParticipationResponse(0, 0, emptyList()),
                ParticipationResponse(0, 0, emptyList())
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