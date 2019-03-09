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
import com.hlandim.marvelheroes.databinding.FragmentHeroesBinding
import com.hlandim.marvelheroes.databinding.HeroItemBinding
import com.hlandim.marvelheroes.model.Hero
import com.hlandim.marvelheroes.view.details.HeroActivity
import com.hlandim.marvelheroes.viewmodel.HeroesViewModel


class HeroesFragment : Fragment(), HeroesAdapter.ListListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHeroesBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProviders.of(this.activity!!).get(HeroesViewModel::class.java)
        val adapter = HeroesAdapter(emptyList<Hero>().toMutableList())
        adapter.listener = this
        binding.lifecycleOwner = this
        this.lifecycle.addObserver(viewModel)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)
                    && !viewModel.isLoading.value!!
                ) {
                    binding.recyclerView.post { adapter.showLoading() }
                    viewModel.requestNextHeroesPage()
                }
            }
        })

        viewModel.isLoading.observe(this, Observer {
            if (it!!) {
                binding.recyclerView.layoutManager?.scrollToPosition(0)
            }
        })

        binding.viewModel = viewModel
        return binding.root
    }

    override fun onRowClicked(binding: HeroItemBinding, hero: Hero) {
        val bundle = Bundle().apply {
            putSerializable("hero", hero)
        }
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    binding.posterImageView,
                    binding.posterImageView.transitionName
                ).toBundle()


                Intent(activity, HeroActivity::class.java)
                    .putExtras(bundle)
                    .let {
                        startActivity(it, options)
                    }
            } else {
                Intent(activity, HeroActivity::class.java)
                    .putExtras(bundle)
                    .let {
                        startActivity(it)
                    }
            }
        }
    }

}