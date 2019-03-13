package com.hlandim.marvelheroes.view.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.databinding.FragmentHeroesBinding
import com.hlandim.marvelheroes.databinding.HeroItemBinding
import com.hlandim.marvelheroes.view.details.HeroActivity
import com.hlandim.marvelheroes.viewmodel.HeroesViewModel
import kotlinx.android.synthetic.main.fragment_heroes.*


class HeroesFragment : Fragment(), HeroesAdapter.ListListener {

    lateinit var mAdapter: HeroesAdapter

    companion object {
        const val REQUEST_CODE = 111
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHeroesBinding.inflate(inflater, container, false)

        activity?.let {
            val viewModel = ViewModelProviders.of(it).get(HeroesViewModel::class.java)
            mAdapter = HeroesAdapter(emptyList<Hero>().toMutableList())
            mAdapter.listener = this
            binding.lifecycleOwner = this
            this.lifecycle.addObserver(viewModel)
            binding.recyclerView.adapter = mAdapter

            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && viewModel.isShowingFavorite.value != null) {
                        val isShowingFavorite = viewModel.isShowingFavorite.value
                        val isLoading = viewModel.isLoading.value
                        if (isShowingFavorite != null && isShowingFavorite) {
                            binding.recyclerView.post { mAdapter.hideLoading() }
                        } else if (isLoading != null && !isLoading) {
                            binding.recyclerView.post { mAdapter.showLoading() }
                            viewModel.requestNextHeroesPage()
                        }
                    }
                }
            })

            viewModel.isLoading.observe(this, Observer { isLoading ->
                if (isLoading != null && isLoading) {
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }
            })

            viewModel.favoritesHeroes.observe(this, Observer {
                //            it?.size
            })

            viewModel.isSearchingMode.observe(this, Observer { isSearchingMode ->
                if (isSearchingMode != null && isSearchingMode) {
                    mAdapter.forceClearList = true
                }
            })

            binding.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
            .asGif()
            .load(R.raw.search_hero_loading)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(loadingGif)
    }

    override fun onRowClicked(binding: HeroItemBinding, hero: Hero, position: Int) {
        val bundle = Bundle().apply {
            putParcelable("hero", hero)
            putInt("position", position)
        }

        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    it,
                    binding.posterImageView,
                    binding.posterImageView.transitionName
                ).toBundle()
                Intent(it, HeroActivity::class.java)
                    .putExtras(bundle)
                    .let { intent ->
                        startActivityForResult(intent, REQUEST_CODE, options)
                    }
            } else {
                Intent(it, HeroActivity::class.java)
                    .putExtras(bundle)
                    .let { intent ->
                        startActivityForResult(intent, REQUEST_CODE)
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val position = data?.getIntExtra("position", -1)
            val hero = data?.getParcelableExtra<Hero>("hero")
            if (hero != null && position != null && position > -1) {
                mAdapter.updateItem(hero, position)
            }

        }
    }

}