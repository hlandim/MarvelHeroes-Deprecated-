package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.model.HeroResponse
import com.hlandim.marvelheroes.util.Tags
import com.hlandim.marvelheroes.util.androidThread
import com.hlandim.marvelheroes.util.ioThread
import com.hlandim.marvelheroes.web.mavel.HeroesRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.HttpException
import java.io.IOException
import java.io.Serializable
import java.net.UnknownHostException

class HeroesViewModel(application: Application, private val heroesRepository: HeroesRepository) :
    AndroidViewModel(application), LifecycleObserver, Serializable, Consumer<Throwable> {


    private val compositeDisposable = CompositeDisposable()

    val heroes: MutableLiveData<MutableList<HeroResponse>> =
        MutableLiveData<MutableList<HeroResponse>>().apply { value = mutableListOf() }
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val communicationError = MutableLiveData<String>()
    private var pageCount: Int = 0

    init {
        RxJavaPlugins.setErrorHandler(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun load() {
        if (heroes.value!!.isEmpty()) {
            getFirstPage()
        }
    }

    private fun getFirstPage() {
        isLoading.value = true
        requestNextHeroesPage()
    }

    fun requestNextHeroesPage() {
        pageCount++
        val disposable = heroesRepository.getHeroes(pageCount)
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .subscribe({
                if (heroes.value!!.isEmpty()) {
                    heroes.value = it.data.results.toMutableList()
                } else {
                    val finalList = heroes.value
                    finalList!!.addAll(it.data.results)
                    heroes.value = finalList
                }

                isLoading.value = false
            }, {
                Log.w(Tags.COMMUNICATION_ERROR, it.message)
                handleCommunicationError(it)
                isLoading.value = false
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

    override fun accept(t: Throwable?) {
        if (t != null) {
            handleCommunicationError(t)
        }
    }
}