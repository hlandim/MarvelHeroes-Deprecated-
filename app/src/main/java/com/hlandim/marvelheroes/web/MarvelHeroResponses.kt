package com.hlandim.marvelheroes.web

import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.database.model.Thumbnail

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


