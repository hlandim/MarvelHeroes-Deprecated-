package com.hlandim.marvelheroes.model

import java.io.Serializable

data class MarvelHeroResponses(
    val code: Int,
    val status: String,
    val data: DataHeroResponse
)

data class DataHeroResponse(
    val offset: Int,
    val limit: Int,
    val total: Long,
    val count: Int,
    val results: List<Hero>
)

data class MarvelParticipationResponses(
    val code: Int,
    val status: String,
    val data: DataParticipationResponse
)

data class DataParticipationResponse(
    val offset: Int,
    val limit: Int,
    val total: Long,
    val count: Int,
    val results: List<ResultParticipationResponse>
)

data class ResultParticipationResponse(
    val title: String,
    val description: String,
    val thumbnail: Thumbnail
)

data class Comic(
    val title: String,
    val description: String
)

class Thumbnail(
    val path: String,
    private val extension: String
) : Serializable {

    fun getFullThumbnailUrl(): String {
        return "$path.$extension"
    }
}

data class ParticipationResponse(
    val available: Int,
    val returned: Int,
    val items: List<Participation>
) : Serializable

class Participation(
    val resourceURI: String,
    val name: String,
    val description: String
) : Serializable