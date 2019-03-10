package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.hlandim.marvelheroes.MyApplication
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.database.model.FavoriteHero
import com.hlandim.marvelheroes.model.Hero
import com.hlandim.marvelheroes.model.ResultParticipationResponse
import com.hlandim.marvelheroes.util.Tags
import com.hlandim.marvelheroes.util.androidThread
import com.hlandim.marvelheroes.util.ioThread
import com.hlandim.marvelheroes.web.mavel.HeroesRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.UnknownHostException

class HeroViewModel(application: Application, private val heroesRepository: HeroesRepository) :
    AndroidViewModel(application), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    val hero: MutableLiveData<Hero> = MutableLiveData()
    val fabResource: MutableLiveData<Int> = MutableLiveData<Int>().apply { value = R.drawable.ic_star }
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val participation: MutableLiveData<ResultParticipationResponse> = MutableLiveData()
    val communicationError = MutableLiveData<String>()


    fun getParticipationDetails(participationPath: String) {

        isLoading.value = true
        communicationError.value = null

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

    fun changeFavoriteHero(view: View) {
        if (hero.value!!.favorite) removeFavoriteHero() else insertFavoriteHero()
    }

    private fun insertFavoriteHero() {
        if (hero.value != null) {
            val favorite = FavoriteHero(id = hero.value!!.id, hero = hero.value!!)
            val disposable =
                Observable.fromCallable { MyApplication.database?.favoriteDao()?.insertFavoriteHero(favorite) }
                    .subscribeOn(ioThread())
                    .observeOn(androidThread())
                    .subscribe({
                        hero.value!!.favorite = true
                        fabResource.value = R.drawable.ic_star_filled
                    }, {
                        handlerError(it)
                    })
            compositeDisposable.add(disposable)

        }
    }

    private fun removeFavoriteHero() {
        if (hero.value != null) {
            val favorite = FavoriteHero(id = hero.value!!.id, hero = hero.value!!)
            MyApplication.database?.favoriteDao()?.removerFavoriteHero(favorite)
            hero.value!!.favorite = false
            fabResource.value = R.drawable.ic_star
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


    private fun handlerError(it: Throwable) {
        Log.w(Tags.COMMUNICATION_ERROR, it.message)
        handleCommunicationError(it)
        isLoading.value = false
    }

    private fun handleCommunicationError(t: Throwable) {
        val message = when (t) {
            is UnknownHostException,
            is IOException -> getApplication<Application>().getString(R.string.network_error)
            is SocketException -> getApplication<Application>().getString(R.string.network_error)
            is HttpException -> getApplication<Application>().getString(R.string.invalid_parameters_error)
            else -> getApplication<Application>().getString(R.string.unknown_error)
        }

        communicationError.value = message
    }
}