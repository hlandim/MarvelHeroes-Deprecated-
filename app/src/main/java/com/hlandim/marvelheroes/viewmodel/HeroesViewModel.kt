package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.model.HeroResponse
import com.hlandim.marvelheroes.model.MarvelResponses
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
    val isEmptySearch: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val communicationError = MutableLiveData<String>()
    var isSearchingMode = false
    private var searchQuery: String? = null
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

        return if (isSearchingMode && searchQuery != null) {
            requestNextSearchPage(searchQuery!!)
        } else {
            requestNextDefaultPage()
        }

    }

    private fun requestNextSearchPage(query: String) {
        if (query != searchQuery) {
            pageCount = 0
        }
        pageCount++
        isSearchingMode = true
        searchQuery = query
        val disposable = heroesRepository.searchHero(query, pageCount)
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .subscribe({
                handleResponse(it)
            }, {
                handlerError(it)
            })

        compositeDisposable.add(disposable)

    }

    fun searchHero(query: String) {
        isLoading.value = true
        isEmptySearch.value = false
        requestNextSearchPage(query)
    }

    fun reload() {
        resetSearchVariables()
        heroes.value!!.clear()
        load()
    }

    private fun resetSearchVariables() {
        if (isSearchingMode) {
            isSearchingMode = false
        }
        pageCount = 0
        searchQuery = null
    }


    private fun requestNextDefaultPage() {
        pageCount++
        isEmptySearch.value = false
        val disposable = heroesRepository.getHeroes(pageCount)
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .subscribe({
                handleResponse(it)
            }, {
                handlerError(it)
            })

        compositeDisposable.add(disposable)
    }

    private fun handlerError(it: Throwable) {
        Log.w(Tags.COMMUNICATION_ERROR, it.message)
        handleCommunicationError(it)
        isLoading.value = false
    }

    private fun handleResponse(it: MarvelResponses) {
        val finalList = heroes.value!!.toMutableList()
        if (pageCount == 1) {
            finalList.clear()
        }
        finalList.addAll(it.data.results)
        heroes.value = finalList
        communicationError.value = null
        isEmptySearch.value = finalList.isNullOrEmpty()
        isLoading.value = false
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