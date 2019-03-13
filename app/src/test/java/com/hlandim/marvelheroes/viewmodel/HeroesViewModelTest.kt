package com.hlandim.marvelheroes.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.web.DataHeroResponse
import com.hlandim.marvelheroes.web.MarvelHeroResponses
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock

class HeroesViewModelTest : ViewModelBaseTest() {

    @Mock
    lateinit var favoriteHeroesObserver: Observer<List<Hero>>

    @Test
    fun `Given HeroesRepository returns next Heroes page when getHeroes(1) called, then update live data`() {
        val heroes = mockHeroes("Hero 1", "Hero 2", "Hero 3")
        val dataHeroResponse = DataHeroResponse(0, 20, heroes.size.toLong(), heroes.size, heroes.toList())
        val response = MarvelHeroResponses(200, "Ok", dataHeroResponse)
        whenever(heroesRepository.getHeroes(1)).thenReturn(Observable.just(response))
        whenever(application.applicationContext).thenReturn(application)

        val heroesViewModel = HeroesViewModel(application)
        heroesViewModel.heroesRepository = heroesRepository

        heroesViewModel.load()

        Assert.assertEquals(heroes, heroesViewModel.heroes.value)

    }

    @Test
    fun `Given HeroesRepository returns Heroes from a search, when searchHero(heroName) called, then update live data`() {
        val heroName = "Hero 1 Search Silva"
        val heroes = mockHeroes(heroName)
        val dataHeroResponse = DataHeroResponse(0, 20, 1, 1, heroes)
        val response = MarvelHeroResponses(200, "Ok", dataHeroResponse)
        whenever(heroesRepository.searchHero(heroName, 1)).thenReturn(Observable.just(response))
        whenever(application.applicationContext).thenReturn(application)

        val heroesViewModel = HeroesViewModel(application)
        heroesViewModel.heroesRepository = heroesRepository

        heroesViewModel.searchHero(heroName)

        Assert.assertEquals(1, heroesViewModel.heroes.value?.size)
        Assert.assertEquals(heroName, heroesViewModel.heroes.value?.get(0)?.name)

    }

    @Test
    fun `Given HeroesRepository returns favorites Heroes, when showFavoritesHeroes(heroName) called, then update live data`() {
        val heroes = mockHeroes("Hero 1", "Hero 2", "Hero 3")
        val favoritesHeroes = mockHeroes("Favorite Hero 1", "Favorite Hero 2", "Favorite Hero 3")
        val liveDataFavoritesHeroes = MutableLiveData<List<Hero>>().apply { value = favoritesHeroes }
        val dataHeroResponse = DataHeroResponse(0, 20, heroes.size.toLong(), heroes.size, heroes)
        val response = MarvelHeroResponses(200, "Ok", dataHeroResponse)
        whenever(heroesRepository.getHeroes(1)).thenReturn(Observable.just(response))
        whenever(heroesRepository.favorites).thenReturn(liveDataFavoritesHeroes)
        whenever(application.applicationContext).thenReturn(application)


        heroesRepository.favorites.observeForever(favoriteHeroesObserver)

        val heroesViewModel = HeroesViewModel(application)
        heroesViewModel.heroesRepository = heroesRepository

        heroesViewModel.load()

        Assert.assertEquals(heroes, heroesViewModel.heroes.value)


        heroesViewModel.showFavoritesHeroes()

        verify(favoriteHeroesObserver).onChanged(favoritesHeroes)


    }
}