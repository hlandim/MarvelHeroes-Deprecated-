package com.hlandim.marvelheroes.web

import com.hlandim.marvelheroes.model.MarvelResponses
import io.reactivex.Observable

interface HeroesDataSource {

    fun getHeroes(page: Int): Observable<MarvelResponses>

    fun searchHero(query: String, page: Int): Observable<MarvelResponses>
}