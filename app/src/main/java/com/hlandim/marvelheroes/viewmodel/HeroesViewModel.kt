package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.util.Tags
import com.hlandim.marvelheroes.util.androidThread
import com.hlandim.marvelheroes.util.ioThread
import com.hlandim.marvelheroes.web.mavel.HeroesRepository
import com.hlandim.marvelheroes.web.mavel.HeroesResponse
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class HeroesViewModel(application: Application, private val heroesRepository: HeroesRepository) :
    AndroidViewModel(application), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    val heroes: MutableLiveData<List<HeroesResponse>> =
        MutableLiveData<List<HeroesResponse>>().apply { value = mutableListOf() }
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val communicationError = MutableLiveData<String>()
    private var pageCount: Int = 0

    init {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun load() {
        if (heroes.value!!.isEmpty()) {
            requestNextHeroesPage()
        }
    }

    fun requestNextHeroesPage() {
        isLoading.value = true
        pageCount++
        val disposable = heroesRepository.getHeroes(pageCount)
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .subscribe({
                heroes.value = it.data.results
                isLoading.value = false


                communicationError.value = it.data.count.toString()


            }, {
                Log.w(Tags.COMMUNICATION_ERROR, it.message)
                handleCommunicationError(it)
            })

        compositeDisposable.add(disposable)
    }

    private fun handleCommunicationError(t: Throwable) {
        val message = when (t.cause) {
            is UnknownHostException,
            is IOException -> getApplication<Application>().getString(R.string.network_error)
            is HttpException -> getApplication<Application>().getString(R.string.invalid_parameters_error)
            else -> getApplication<Application>().getString(R.string.unknown_error)
        }

        communicationError.value = message
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}