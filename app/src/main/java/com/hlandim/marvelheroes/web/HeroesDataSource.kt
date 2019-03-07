package com.hlandim.marvelheroes.web

import com.hlandim.marvelheroes.web.mavel.MarvelResponses
import io.reactivex.Observable

interface HeroesDataSource {

    fun getHeroes(page: Int): Observable<MarvelResponses>
}