package com.hlandim.marvelheroes.web.mavel

import com.hlandim.marvelheroes.model.MarvelResponses
import com.hlandim.marvelheroes.web.HeroesDataSource
import io.reactivex.Observable

class HeroesRepository(private val dataSource: HeroesDataSource) : HeroesDataSource {

    override fun searchHero(query: String, page: Int): Observable<MarvelResponses> {
        return dataSource.searchHero(query, page)
    }

    override fun getHeroes(page: Int): Observable<MarvelResponses> {
        return dataSource.getHeroes(page)
    }
}