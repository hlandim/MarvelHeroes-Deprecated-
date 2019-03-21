package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import android.view.View
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.base.BaseViewModel
import com.hlandim.marvelheroes.database.AppDataBase
import com.hlandim.marvelheroes.database.HeroesRepository
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.util.Tags
import com.hlandim.marvelheroes.util.androidThread
import com.hlandim.marvelheroes.util.ioThread
import com.hlandim.marvelheroes.web.ResultParticipationResponse
import com.hlandim.marvelheroes.web.mavel.HeroesService
import com.hlandim.marvelheroes.web.mavel.MarvelApi
import io.reactivex.Observable

class HeroViewModel(application: Application) :
    BaseViewModel(application), LifecycleObserver {


    val hero: MutableLiveData<Hero> = MutableLiveData()
    val fabResource: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = R.drawable.ic_star }
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val participation: MutableLiveData<ResultParticipationResponse> = MutableLiveData()
    var heroesRepository: HeroesRepository

    init {
        val heroesService = HeroesService(MarvelApi.create(application))
        heroesRepository = HeroesRepository(
            heroesService,
            AppDataBase.getDataBase(application).favoriteDao()
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun load() {
        setFabIcon()
    }

    private fun setFabIcon() {
        hero.value?.let {
            fabResource.value = hero.value?.getFavoriteImage()
        }
    }


    fun getParticipationDetails(participationPath: String) {

        isLoading.value = true

        val disposable = heroesRepository.getParticipationDetails(participationPath)
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .subscribe({
                participation.value = it.data.results[0]
                isLoading.value = false
            }, {
                isLoading.value = false
                handlerError(it)
            })

        compositeDisposable.add(disposable)

    }

    fun changeFavoriteHero(view: View?) {
        val hero = hero.value
        if (hero != null && hero.favorite) removeFavoriteHero() else insertFavoriteHero()
    }

    private fun insertFavoriteHero() {
        hero.value?.let {
            val disposable =
                Observable.fromCallable { heroesRepository.insertFavoriteHero(it) }
                    .subscribeOn(ioThread())
                    .observeOn(androidThread())
                    .subscribe({
                        hero.value?.let {
                            updateFabImage(it)
                        }
                    }, {
                        handlerError(it)
                    })
            compositeDisposable.add(disposable)

        }
    }

    private fun removeFavoriteHero() {
        hero.value?.let {
            val disposable =
                Observable.fromCallable { heroesRepository.removerFavoriteHero(it) }
                    .subscribeOn(ioThread())
                    .observeOn(androidThread())
                    .subscribe({
                        hero.value?.let {
                            updateFabImage(it)
                        }
                    }, {
                        handlerError(it)
                    })
            compositeDisposable.add(disposable)
        }
    }

    private fun updateFabImage(it: Hero) {
        fabResource.value = it.getFavoriteImage()
    }

    private fun handlerError(it: Throwable) {
        Log.w(Tags.COMMUNICATION_ERROR, it.message)
        handleCommunicationError(it)
        isLoading.value = false

    }

}