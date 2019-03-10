package com.hlandim.marvelheroes.database

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.hlandim.marvelheroes.database.dao.FavoriteDao
import com.hlandim.marvelheroes.database.model.FavoriteHero
import com.hlandim.marvelheroes.model.Hero
import com.hlandim.marvelheroes.model.MarvelHeroResponses
import com.hlandim.marvelheroes.model.MarvelParticipationResponses
import com.hlandim.marvelheroes.util.androidThread
import com.hlandim.marvelheroes.util.ioThread
import com.hlandim.marvelheroes.web.HeroesDataSource
import io.reactivex.Observable

class HeroesRepository(private val dataSource: HeroesDataSource, private val favoriteDao: FavoriteDao) :
    HeroesDataSource {

    val favorites: LiveData<List<FavoriteHero>> = favoriteDao.getAllFavoriteHeroes()

    override fun getParticipationDetails(path: String): Observable<MarvelParticipationResponses> {
        return dataSource.getParticipationDetails(path)
    }

    override fun searchHero(query: String, page: Int): Observable<MarvelHeroResponses> {
        return dataSource.searchHero(query, page)
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .doOnNext {
                markFavoriteHeroes(it.data.results)
            }
    }

    override fun getHeroes(page: Int): Observable<MarvelHeroResponses> {
        return dataSource.getHeroes(page).subscribeOn(ioThread())
            .observeOn(androidThread())
            .doOnNext {
                markFavoriteHeroes(it.data.results)
            }
    }

    @WorkerThread
    fun insertFavoriteHero(favoriteHero: FavoriteHero) {
        favoriteDao.insertFavoriteHero(favoriteHero)
    }

    @WorkerThread
    fun removerFavoriteHero(favoriteHero: FavoriteHero) {
        favoriteDao.removerFavoriteHero(favoriteHero)
    }

    private fun markFavoriteHeroes(heroes: List<Hero>) {
        val common = heroes.toMutableList()
        val favorites = favorites.value?.map { it.hero }?.toMutableList()
        if (!favorites.isNullOrEmpty()) {
            common.retainAll(favorites)
            common.forEach { it.favorite = true }
        }
    }
}