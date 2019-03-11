package com.hlandim.marvelheroes.web.mavel

import com.hlandim.marvelheroes.web.MarvelHeroResponses
import com.hlandim.marvelheroes.web.MarvelParticipationResponses
import com.hlandim.marvelheroes.web.HeroesDataSource
import io.reactivex.Observable

class HeroesService(private val api: MarvelApi) : HeroesDataSource {
    override fun getParticipationDetails(path: String): Observable<MarvelParticipationResponses> {
        return api.getParticipationDetails(path = path.removePrefix(MarvelApi.URL))
    }

    override fun searchHero(query: String, page: Int): Observable<MarvelHeroResponses> {
        val offset = (page - 1) * MarvelApi.LIMIT_RESULT_SIZE
        return api.getHeroes(offset = offset, nameStartsWith = query)
    }

    override fun getHeroes(page: Int): Observable<MarvelHeroResponses> {
        val offset = (page - 1) * MarvelApi.LIMIT_RESULT_SIZE
        return api.getHeroes(offset = offset, nameStartsWith = null)
    }


}