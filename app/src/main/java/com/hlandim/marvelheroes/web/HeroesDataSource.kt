package com.hlandim.marvelheroes.web

import io.reactivex.Observable

interface HeroesDataSource {

    fun getHeroes(page: Int): Observable<MarvelHeroResponses>

    fun searchHero(query: String, page: Int): Observable<MarvelHeroResponses>

    fun getParticipationDetails(path: String): Observable<MarvelParticipationResponses>
}