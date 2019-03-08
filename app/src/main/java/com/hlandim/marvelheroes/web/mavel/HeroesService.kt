package com.hlandim.marvelheroes.web.mavel

import com.hlandim.marvelheroes.model.MarvelResponses
import com.hlandim.marvelheroes.util.md5
import com.hlandim.marvelheroes.web.HeroesDataSource
import io.reactivex.Observable
import java.util.*

class HeroesService(private val api: MarvelApi) : HeroesDataSource {

    override fun searchHero(query: String, page: Int): Observable<MarvelResponses> {
        val timeStamp = Date().time
        val offset = (page - 1) * MarvelApi.LIMIT_RESULT_SIZE
        return api.getHeroes(ts = timeStamp, hash = getHashMd5(timeStamp), offset = offset, nameStartsWith = query)
    }

    override fun getHeroes(page: Int): Observable<MarvelResponses> {
        val timeStamp = Date().time
        val offset = (page - 1) * MarvelApi.LIMIT_RESULT_SIZE
        return api.getHeroes(ts = timeStamp, hash = getHashMd5(timeStamp), offset = offset, nameStartsWith = null)
    }

    private fun getHashMd5(timeStamp: Any): String {

        val bodyHash = timeStamp.toString() + MarvelApi.API_KEY_PRIVATE + MarvelApi.API_KEY_PUBLIC
        return bodyHash.md5()
    }
}