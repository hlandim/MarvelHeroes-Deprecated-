package com.hlandim.marvelheroes.web.mavel

import com.hlandim.marvelheroes.model.MarvelParticipationResponses
import com.hlandim.marvelheroes.model.MarvelHeroResponses
import com.hlandim.marvelheroes.web.HeroesDataSource
import io.reactivex.Observable

class HeroesRepository(private val dataSource: HeroesDataSource) : HeroesDataSource {

    override fun getParticipationDetails(path: String): Observable<MarvelParticipationResponses> {
        return dataSource.getParticipationDetails(path)
    }

    override fun searchHero(query: String, page: Int): Observable<MarvelHeroResponses> {
        return dataSource.searchHero(query, page)
    }

    override fun getHeroes(page: Int): Observable<MarvelHeroResponses> {
        return dataSource.getHeroes(page)
    }
}