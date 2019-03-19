package com.hlandim.marvelheroes.database

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.hlandim.marvelheroes.database.dao.FavoriteDao
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.web.HeroesDataSource
import com.hlandim.marvelheroes.web.MarvelHeroResponses
import com.hlandim.marvelheroes.web.MarvelParticipationResponses
import io.reactivex.Observable

class HeroesRepository(
    private val dataSource: HeroesDataSource,
    var favoriteDao: FavoriteDao
) :
    HeroesDataSource {

    val favorites: LiveData<List<Hero>> = favoriteDao.getAllFavoriteHeroes()

    override fun getParticipationDetails(path: String): Observable<MarvelParticipationResponses> {
        return dataSource.getParticipationDetails(path)
    }

    override fun searchHero(query: String, page: Int): Observable<MarvelHeroResponses> {
        return dataSource.searchHero(query, page)
            .doOnNext {
                markFavoriteHeroes(it.data.results)
            }
    }

    override fun getHeroes(page: Int): Observable<MarvelHeroResponses> {
        return dataSource.getHeroes(page)
            .doOnNext {
                markFavoriteHeroes(it.data.results)
            }
    }

    @WorkerThread
    fun insertFavoriteHero(hero: Hero) {
        hero.favorite = true
        favoriteDao.insertFavoriteHero(hero)

    }

    @WorkerThread
    fun removerFavoriteHero(hero: Hero) {
        hero.favorite = false
        favoriteDao.removerFavoriteHero(hero)

    }

    private fun markFavoriteHeroes(heroes: List<Hero>) {
        val common = heroes.toMutableList()
        val favorites = favorites.value
        if (!favorites.isNullOrEmpty()) {
            common.retainAll(favorites)
            common.forEach { it.favorite = true }
        }
    }
}