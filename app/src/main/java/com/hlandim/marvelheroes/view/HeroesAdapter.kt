package com.hlandim.marvelheroes.view

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hlandim.marvelheroes.databinding.HeroItemBinding
import com.hlandim.marvelheroes.databinding.RowLoadingBinding
import com.hlandim.marvelheroes.model.HeroResponse
import kotlinx.android.synthetic.main.row_loading.view.*

class HeroesAdapter(private var hereos: MutableList<HeroResponse>) :
    RecyclerView.Adapter<HeroesAdapter.CustomViewHolder>() {

    lateinit var listener: ListListener
    private var hideLoading = false

    companion object {
        const val ITEM = 1
        const val LOADING = 2
    }

    init {
    }

    abstract class CustomViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(hero: HeroResponse)
    }

    class ViewHolder(private val binding: HeroItemBinding) : CustomViewHolder(binding) {
        override fun bind(hero: HeroResponse) {
            binding.hero = hero
        }
    }

    class FooterHolder(binding: RowLoadingBinding) : CustomViewHolder(binding) {
        override fun bind(hero: HeroResponse) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM) {
            val binding = HeroItemBinding.inflate(layoutInflater, parent, false)
            ViewHolder(binding)
        } else {
            val binding = RowLoadingBinding.inflate(layoutInflater, parent, false)
            FooterHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (position < hereos.size) {
            val movie = hereos[position]
            holder.bind(movie)
            holder.itemView.setOnClickListener {
                listener.onRowClicked(movie)
            }
        } else {
            val footerHolder = holder as FooterHolder
            if (!hideLoading && hereos.size > 0) {
                footerHolder.itemView.pbLoadingNewHeroes.visibility = View.VISIBLE
            } else {
                footerHolder.itemView.pbLoadingNewHeroes.visibility = View.INVISIBLE
            }
        }
    }

    fun hideLoading() {
        hideLoading = true
        notifyDataSetChanged()
    }

    fun showLoading() {
        hideLoading = false
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return hereos.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == hereos.size) LOADING else ITEM
    }

    interface ListListener {
        fun onRowClicked(hero: HeroResponse)
    }

    fun replaceItems(heroes: MutableList<HeroResponse>) {
        this.hereos = heroes
        hideLoading = true
        notifyDataSetChanged()
    }
}
