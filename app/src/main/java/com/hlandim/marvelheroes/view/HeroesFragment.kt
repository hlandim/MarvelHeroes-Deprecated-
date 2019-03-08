package com.hlandim.marvelheroes.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hlandim.marvelheroes.databinding.FragmentHeroesBinding
import com.hlandim.marvelheroes.model.HeroResponse
import com.hlandim.marvelheroes.viewmodel.HeroesViewModel

class HeroesFragment : Fragment(), HeroesAdapter.ListListener {


    companion object {
        fun newInstance(viewModel: HeroesViewModel): HeroesFragment {
            return HeroesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("viewmodel", viewModel)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHeroesBinding.inflate(inflater, container, false)
        val viewModel = arguments?.getSerializable("viewmodel") as HeroesViewModel
        val adapter = HeroesAdapter(emptyList<HeroResponse>().toMutableList())
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
                    adapter.showLoading()
                    viewModel.requestNextHeroesPage()
                }
            }
        })
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onRowClicked(hero: HeroResponse) {
        Toast.makeText(context, hero.name, Toast.LENGTH_SHORT).show()
    }

}