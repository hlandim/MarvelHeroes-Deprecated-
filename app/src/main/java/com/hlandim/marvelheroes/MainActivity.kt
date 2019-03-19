package com.hlandim.marvelheroes

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.hlandim.marvelheroes.util.AppPermissionManager
import com.hlandim.marvelheroes.util.getViewModel
import com.hlandim.marvelheroes.view.list.HeroesFragment
import com.hlandim.marvelheroes.view.list.MySuggestionProvider
import com.hlandim.marvelheroes.viewmodel.HeroesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: HeroesViewModel
    private lateinit var searchView: SearchView
    private var showingFavorites = false
    private val permissionManager = AppPermissionManager(this)
    private lateinit var mFavoriteButton: MenuItem

    companion object {
        const val FRAGMENT_TAG = "fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = createViewModel()
        if (permissionManager.checkAndRequestPermission()) {
            initApp()
        }
    }

    private fun initApp() {

        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentLayout, HeroesFragment(), FRAGMENT_TAG).commit()
        } else {
            supportFragmentManager.beginTransaction().show(fragment).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.options_menu, menu)

        configureFavoritesButton(menu)

        configureSearchView(menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun configureFavoritesButton(menu: Menu) {
        mFavoriteButton = menu.findItem(R.id.favorites)
        mFavoriteButton.setOnMenuItemClickListener {
            if (showingFavorites) {
                mViewModel.hideFavoritesHeroes()
                setFavoriteButtonImg(R.drawable.ic_star)
            } else {
                mViewModel.showFavoritesHeroes()
                setFavoriteButtonImg(R.drawable.ic_star_filled)
            }
            showingFavorites = !showingFavorites
            false
        }

        mViewModel.isSearchingMode.observe(this, Observer {
            it?.let {
                setFavoriteButtonImg(R.drawable.ic_star)
            }
        })
    }

    private fun configureSearchView(menu: Menu) {
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
                val isSearchingMode = mViewModel.isSearchingMode.value
                if (isSearchingMode != null && isSearchingMode || mViewModel.heroes.value.isNullOrEmpty()) {
                    restartList()
                }
                return true
            }

        })
    }

    private fun restartList() {
        setFavoriteButtonImg(R.drawable.ic_star)
        showingFavorites = false
        mViewModel.reload()
    }

    private fun setFavoriteButtonImg(res: Int) {
        mFavoriteButton.setIcon(res)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.handlePermissionResponse(requestCode, permissions, grantResults, {
            initApp()
        }, {})

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleIntent(it)
        }
    }

    private fun handleIntent(intent: Intent) {
        val isLoading = mViewModel.isLoading.value
        if (Intent.ACTION_SEARCH == intent.action && isLoading != null && !isLoading) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE).saveRecentQuery(
                query,
                null
            )
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
