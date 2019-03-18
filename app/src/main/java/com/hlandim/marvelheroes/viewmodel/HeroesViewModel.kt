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
import io.reactivex.Observable
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
    private var heroesCache: MutableList<Hero>? = mutableListOf()
    val favoritesHeroes: LiveData<List<Hero>>
    val isShowingFavorite: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val isEmptySearch: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val communicationError = MutableLiveData<String>()
    var heroesRepository: HeroesRepository
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
        val heroes = heroes.value
        if (heroes != null && heroes.isEmpty()) {
            getFirstPage()
        }
    }

    private fun getFirstPage() {
        isLoading.value = true
        requestNextHeroesPage()
    }

    fun showFavoritesHeroes() {
        heroes.value?.let {
            heroesCache = it.toMutableList()
        }
        this.heroes.value = favoritesHeroes.value?.toMutableList()
        isShowingFavorite.value = true
        isLoading.value = false
    }

    fun hideFavoritesHeroes() {
        heroes.value?.clear()
        heroes.value = heroesCache
        isShowingFavorite.value = false
    }

    fun requestNextHeroesPage() {
        val isSearching = isSearchingMode.value
        val searchQuery = searchQuery
        return if (isSearching != null && isSearching && searchQuery != null) {
            requestNextSearchPage(searchQuery)
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
        isShowingFavorite.value = false
        requestNextSearchPage(query.trim())
    }

    fun reload() {
        resetSearchVariables()
        isShowingFavorite.value = false
        heroes.value?.clear()
        load()
    }

    private fun resetSearchVariables() {
        val isSearchingMode = isSearchingMode.value
        if (isSearchingMode != null && isSearchingMode) {
            this.isSearchingMode.value = false
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

    fun changeFavoriteHero(hero: Hero): Observable<Unit>? {
        return if (hero.favorite) removeFavoriteHero(hero) else insertFavoriteHero(hero)
    }

    private fun insertFavoriteHero(hero: Hero): Observable<Unit>? {

        return Observable.fromCallable { heroesRepository.insertFavoriteHero(hero) }
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .doOnError {
                handlerError(it)
            }


    }

    private fun removeFavoriteHero(hero: Hero): Observable<Unit>? {
        return Observable.fromCallable { heroesRepository.removerFavoriteHero(hero) }
            .subscribeOn(ioThread())
            .observeOn(androidThread())
            .doOnError {
                handlerError(it)
            }
    }

    private fun handleResponse(it: MarvelHeroResponses) {
        val finalList = heroes.value?.toMutableList()
        if (pageCount == 1) {
            finalList?.clear()
        }
        finalList?.addAll(it.data.results)
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
        compositeDisposable.dispose()
        super.onCleared()
    }

    override fun accept(t: Throwable?) {
        t?.let {
            handleCommunicationError(it)
        }
    }
}