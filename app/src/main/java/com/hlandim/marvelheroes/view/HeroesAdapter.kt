package com.hlandim.marvelheroes.view

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hlandim.marvelheroes.databinding.HeroItemBinding
import com.hlandim.marvelheroes.databinding.RowLoadingBinding
import com.hlandim.marvelheroes.model.Hero
import kotlinx.android.synthetic.main.row_loading.view.*

class HeroesAdapter(private var hereos: MutableList<Hero>) :
    RecyclerView.Adapter<HeroesAdapter.CustomViewHolder>() {

    lateinit var listener: ListListener

    companion object {
        const val ITEM = 1
        const val LOADING = 2
    }

    init {
    }

    abstract class CustomViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(hero: Hero)
    }

    class ViewHolder(val binding: HeroItemBinding) : CustomViewHolder(binding) {
        override fun bind(hero: Hero) {
            binding.hero = hero
        }
    }

    class FooterHolder(binding: RowLoadingBinding) : CustomViewHolder(binding) {
        override fun bind(hero: Hero) {

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
            val rowHolder = holder as ViewHolder
            rowHolder.bind(movie)
            rowHolder.itemView.setOnClickListener {
                listener.onRowClicked(rowHolder.binding, movie)
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.size > 0) {
            val footerHolder = holder as FooterHolder
            val footerControl = payloads[0] as FooterControl
            if (footerControl.showLoading) {
                footerHolder.itemView.pbLoadingNewHeroes.visibility = View.VISIBLE
            } else {
                footerHolder.itemView.pbLoadingNewHeroes.visibility = View.INVISIBLE
            }

            if (footerControl.showNoMoreResult) {
                footerHolder.itemView.tvNoMoreHeroes.visibility = View.VISIBLE
            } else {
                footerHolder.itemView.tvNoMoreHeroes.visibility = View.INVISIBLE
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    fun hideLoading() {
        notifyItemChanged(hereos.size, FooterControl(showLoading = false, showNoMoreResult = false))
    }

    fun showLoading() {
        notifyItemChanged(hereos.size, FooterControl(showLoading = true, showNoMoreResult = false))
    }


    override fun getItemCount(): Int {
        return hereos.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == hereos.size) LOADING else ITEM
    }

    interface ListListener {
        fun onRowClicked(binding: HeroItemBinding, hero: Hero)
    }

    fun replaceItems(newHeroes: MutableList<Hero>) {
        val noMoreResult = this.hereos == newHeroes
        val actualSize = hereos.size
        this.hereos = newHeroes
        when {
            noMoreResult -> notifyItemChanged(actualSize, FooterControl(showLoading = false, showNoMoreResult = true))
            actualSize == 0 -> notifyDataSetChanged()
            newHeroes.size > actualSize -> notifyItemRangeChanged(actualSize, hereos.size - 1)
            else -> notifyDataSetChanged()
        }
    }

    private data class FooterControl(val showLoading: Boolean, val showNoMoreResult: Boolean)
}
