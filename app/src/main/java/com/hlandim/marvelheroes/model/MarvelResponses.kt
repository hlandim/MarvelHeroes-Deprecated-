package com.hlandim.marvelheroes.model

import java.io.Serializable

data class MarvelResponses(
    val code: Int,
    val status: String,
    val data: DataResponse
)

data class DataResponse(
    val offset: Int,
    val limit: Int,
    val total: Long,
    val count: Int,
    val results: List<Hero>
)

data class Thumbnail(
    val path: String,
    val extension: String
) : Serializable

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