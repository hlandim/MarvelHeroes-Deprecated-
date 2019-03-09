package com.hlandim.marvelheroes.view.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hlandim.marvelheroes.databinding.HeroParticipationListRowBinding
import com.hlandim.marvelheroes.model.Participation

class ParticipationAdapter(private val context: Context, var list: List<Participation>) : BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val rowView: View
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent!!.context)
            val binding = HeroParticipationListRowBinding.inflate(layoutInflater, parent, false)
            holder = ViewHolder(binding)
            rowView = binding.root
            rowView.tag = holder
        } else {
            rowView = convertView
            holder = rowView.tag as ViewHolder
        }
        holder.bind(list[position])
        return rowView

    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: HeroParticipationListRowBinding) {
        fun bind(participation: Participation) {
            binding.paticipation = participation
        }
    }

    fun setParticipation(list: List<Participation>) {
        this.list = list
        notifyDataSetChanged()
    }
}