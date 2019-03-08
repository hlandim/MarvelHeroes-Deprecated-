package com.hlandim.marvelheroes.model

import java.util.*

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
    val results: List<HeroResponse>
)

data class HeroResponse(
    val id: Int,
    val name: String,
    val description: String,
    val modified: Date,
    val thumbnail: Thumbnail,
    val comics: Comics
)

data class Thumbnail(
    val path: String,
    val extension: String
)

data class Comics(
    val available: Int,
    val returned: Int,
    val items: List<Comic>
)

data class Comic(
    val resourceURI: String,
    val name: String
)