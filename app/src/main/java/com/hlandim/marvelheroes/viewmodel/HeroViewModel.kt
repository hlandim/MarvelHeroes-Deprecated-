package com.hlandim.marvelheroes.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import com.hlandim.marvelheroes.model.Hero
import com.hlandim.marvelheroes.web.mavel.HeroesRepository
import java.io.Serializable

class HeroViewModel(application: Application, private val heroesRepository: HeroesRepository) :
    AndroidViewModel(application), LifecycleObserver, Serializable {

    val hero: MutableLiveData<Hero> = MutableLiveData()
}