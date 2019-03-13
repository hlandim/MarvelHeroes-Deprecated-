package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.arctouch.codechallenge.util.RxImmediateSchedulerRule
import com.hlandim.marvelheroes.database.HeroesRepository
import com.hlandim.marvelheroes.database.model.Hero
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

open class ViewModelBaseTest {

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
    open fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    fun mockHeroes(vararg names: String): MutableList<Hero> {

        val heroes = mutableListOf<Hero>()
        names.forEachIndexed { index, name ->
            heroes.add(Hero(index, name))
        }

        return heroes
    }

}