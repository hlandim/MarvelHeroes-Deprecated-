package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.arctouch.codechallenge.util.RxImmediateSchedulerRule
import com.hlandim.marvelheroes.database.HeroesRepository
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.web.DataHeroResponse
import com.hlandim.marvelheroes.web.MarvelHeroResponses
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HeroesViewModelTest {

    @Mock
    lateinit var heroesRepository: HeroesRepository

    @Mock
    lateinit var application: Application

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }


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

    private fun mockHeroes(vararg names: String): MutableList<Hero> {

        val heroes = mutableListOf<Hero>()
        names.forEachIndexed { index, name ->
            heroes.add(Hero(index, name))
        }

        return heroes
    }
}