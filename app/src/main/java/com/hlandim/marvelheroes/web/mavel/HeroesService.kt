package com.hlandim.marvelheroes.web.mavel

import com.hlandim.marvelheroes.util.md5
import com.hlandim.marvelheroes.web.HeroesDataSource
import io.reactivex.Observable
import java.util.*

class HeroesService(private val api: MarvelApi) : HeroesDataSource {

    override fun getHeroes(page: Int): Observable<MarvelResponses> {
        val timeStamp = Date().time
        val bodyHash = timeStamp.toString() + MarvelApi.API_KEY_PRIVATE + MarvelApi.API_KEY_PUBLIC
        return api.getHeroes(ts = timeStamp, hash = bodyHash.md5())
    }
}