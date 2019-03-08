package com.hlandim.marvelheroes

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hlandim.marvelheroes.databinding.ActivityMainBinding
import com.hlandim.marvelheroes.util.getViewModel
import com.hlandim.marvelheroes.view.HeroesFragment
import com.hlandim.marvelheroes.viewmodel.HeroesViewModel
import com.hlandim.marvelheroes.web.mavel.HeroesRepository
import com.hlandim.marvelheroes.web.mavel.HeroesService
import com.hlandim.marvelheroes.web.mavel.MarvelApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val mBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        mBinding.lifecycleOwner = this
//        val viewModel = createViewModel()
//        mBinding.viewModel = viewModel
//        this.lifecycle.addObserver(viewModel)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, HeroesFragment.newInstance(createViewModel())).commit()
    }


    private fun createViewModel(): HeroesViewModel {
        val heroesService = HeroesService(MarvelApi.create())
        val heroesRepository = HeroesRepository(heroesService)
        return getViewModel { HeroesViewModel(application, heroesRepository) }
    }

}
