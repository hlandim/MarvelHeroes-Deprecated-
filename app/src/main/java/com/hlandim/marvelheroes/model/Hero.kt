package com.hlandim.marvelheroes.model

import java.io.Serializable
import java.util.*

class Hero(
    val id: Int,
    val name: String,
    val description: String,
    val modified: Date,
    val thumbnail: Thumbnail,
    val resourceURI: String,
    val comics: ParticipationResponse,
    val series: ParticipationResponse,
    val stories: ParticipationResponse,
    val events: ParticipationResponse
) : Serializable {

    fun getFullThumbnailUrl(): String {
        return thumbnail.path + "." + thumbnail.extension
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Hero)
            return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id + 30
    }
}