package com.hlandim.marvelheroes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.hlandim.marvelheroes.database.AppDataBase
import com.hlandim.marvelheroes.database.HeroesRepository
import com.hlandim.marvelheroes.util.getViewModel
import com.hlandim.marvelheroes.view.list.HeroesFragment
import com.hlandim.marvelheroes.viewmodel.HeroesViewModel
import com.hlandim.marvelheroes.web.mavel.HeroesService
import com.hlandim.marvelheroes.web.mavel.MarvelApi

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: HeroesViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = createViewModel()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, HeroesFragment()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem = menu.findItem(R.id.search)
        searchView = menuItem.actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (mViewModel.isSearchingMode || mViewModel.heroes.value.isNullOrEmpty()) {
                    mViewModel.reload()
                }
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action && !mViewModel.isLoading.value!!) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            mViewModel.searchHero(query)

        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }

    private fun createViewModel(): HeroesViewModel {
        return getViewModel { HeroesViewModel(application) }
    }

}
