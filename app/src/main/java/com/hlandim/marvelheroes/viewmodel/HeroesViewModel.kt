package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.database.AppDataBase
import com.hlandim.marvelheroes.database.HeroesRepository
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.util.Tags
import com.hlandim.marvelheroes.util.androidThread
import com.hlandim.marvelheroes.util.ioThread
import com.hlandim.marvelheroes.web.MarvelHeroResponses
import com.hlandim.marvelheroes.web.mavel.HeroesService
import com.hlandim.marvelheroes.web.mavel.MarvelApi
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class HeroesViewModel(application: Application) :
    AndroidViewModel(application), LifecycleObserver, Consumer<Throwable> {


    private val compositeDisposable = CompositeDisposable()

    val heroes: MutableLiveData<MutableList<Hero>> =
        MutableLiveData<MutableList<Hero>>().apply { value = mutableListOf() }
    var heroesCache: MutableList<Hero>? = mutableListOf()
    val favoritesHeroes: LiveData<List<Hero>>
    val isShowingFavorite: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val isEmptySearch: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val communicationError = MutableLiveData<String>()
    private val heroesRepository: HeroesRepository
    var isSearchingMode: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var searchQuery: String? = null
    private var pageCount: Int = 0

    init {
        RxJavaPlugins.setErrorHandler(this)
        val heroesService = HeroesService(MarvelApi.create())
        heroesRepository = HeroesRepository(
            heroesService,
            AppDataBase.getDataBase(application).favoriteDao()
        )
        favoritesHeroes = heroesRepository.favorites
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

    fun showFavoritesHeroes() {
        heroesCache = heroes.value!!.toMutableList()
        heroes.value = favoritesHeroes.value?.toMutableList()
        isShowingFavorite.value = true
    }

    fun hideFavoritesHeroes() {
        heroes.value?.clear()
        heroes.value = heroesCache
        isShowingFavorite.value = false
    }

    fun requestNextHeroesPage() {

        return if (isSearchingMode.value!! && searchQuery != null) {
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
        resetSearchVariables()
        isLoading.value = true
        isEmptySearch.value = false
        isSearchingMode.value = true
        requestNextSearchPage(query)
    }

    fun reload() {
        resetSearchVariables()
        heroes.value!!.clear()
        load()
    }

    private fun resetSearchVariables() {
        if (isSearchingMode.value!!) {
            isSearchingMode.value = false
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

    private fun handleResponse(it: MarvelHeroResponses) {
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
        val message = when (t) {
            is UnknownHostException,
            is IOException -> getApplication<Application>().getString(R.string.network_error)
            is HttpException -> getApplication<Application>().getString(R.string.invalid_parameters_error)
            else -> getApplication<Application>().getString(R.string.unknown_error)
        }

        communicationError.value = message
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    override fun accept(t: Throwable?) {
        if (t != null) {
            handleCommunicationError(t)
        }
    }
}