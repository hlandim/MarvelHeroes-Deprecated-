package com.hlandim.marvelheroes.viewmodel

import android.arch.persistence.room.Room
import com.hlandim.marvelheroes.database.AppDataBase
import com.hlandim.marvelheroes.database.dao.FavoriteDao
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert
import org.junit.Test


class HeroViewModelTest : ViewModelBaseTest() {

    private lateinit var favoriteDao: FavoriteDao
    private lateinit var appDataBase: AppDataBase

    override fun setUp() {
        super.setUp()

        appDataBase =
            Room.inMemoryDatabaseBuilder(application, AppDataBase::class.java).allowMainThreadQueries().build()
        favoriteDao = appDataBase.favoriteDao()

    }

    @Test
    fun `Given a Hero mark him as favorite, then update live data`() {
        val hero = mockHeroes("Hero 1")[0]
        hero.favorite = false

        whenever(application.applicationContext).thenReturn(application)

        val heroViewModel = HeroViewModel(application)
        heroViewModel.heroesRepository.favoriteDao = favoriteDao
        heroViewModel.hero.value = hero

        heroViewModel.changeFavoriteHero(null)

        Assert.assertTrue(heroViewModel.hero.value != null)
        heroViewModel.hero.value?.let {
            Assert.assertTrue(it.favorite)
            Assert.assertEquals(hero.name, it.name)
        }
    }

    @Test
    fun `Given a Hero unmark him as favorite, then update live data`() {

        val hero = mockHeroes("Hero 1")[0]
        hero.favorite = true

        whenever(application.applicationContext).thenReturn(application)

        val heroViewModel = HeroViewModel(application)
        heroViewModel.heroesRepository.favoriteDao = favoriteDao
        heroViewModel.hero.value = hero

        heroViewModel.changeFavoriteHero(null)

        Assert.assertTrue(heroViewModel.hero.value != null)
        heroViewModel.hero.value?.let {
            Assert.assertFalse(it.favorite)
            Assert.assertEquals(hero.name, it.name)
        }
    }


}