package com.hlandim.marvelheroes.view.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.hlandim.marvelheroes.database.model.Participation
import com.hlandim.marvelheroes.databinding.ParticipationListChildLayoutBinding
import com.hlandim.marvelheroes.databinding.ParticipationListGroupLayoutBinding

class ParticipationAdapter(
    private var participationGroupList: List<ParticipationParent>,
    private var listener: ParticipationListener
) :
    BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): ParticipationParent {
        return participationGroupList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var rowView = convertView
        val holderGroup: ViewHolderGroup
        if (rowView == null) {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val binding = ParticipationListGroupLayoutBinding.inflate(layoutInflater, parent, false)
            holderGroup = ViewHolderGroup(binding)
            rowView = binding.root
            rowView.tag = holderGroup
        } else {
            holderGroup = rowView.tag as ViewHolderGroup
        }

        holderGroup.bind(participationGroupList[groupPosition].title)
        return rowView

    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return participationGroupList[groupPosition].participation.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return participationGroupList[groupPosition].participation[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var rowView = convertView
        val holderGroup: ViewHolderChild
        if (rowView == null) {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val binding = ParticipationListChildLayoutBinding.inflate(layoutInflater, parent, false)
            holderGroup = ViewHolderChild(binding)
            rowView = binding.root
            rowView.tag = holderGroup
        } else {
            holderGroup = rowView.tag as ViewHolderChild
        }
        val participation = participationGroupList[groupPosition].participation[childPosition]
        holderGroup.bind(participation.name)
        rowView.setOnClickListener {
            listener.onParticipationClicked(participation)
        }
        return rowView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return participationGroupList.size
    }

    fun setParticipation(participation: List<ParticipationParent>) {
        this.participationGroupList = participation
        notifyDataSetChanged()
    }

    private class ViewHolderGroup(val participationGroup: ParticipationListGroupLayoutBinding) {
        fun bind(title: String) {
            participationGroup.title = title
        }
    }

    private class ViewHolderChild(val participation: ParticipationListChildLayoutBinding) {
        fun bind(title: String) {
            participation.title = title
        }
    }

    interface ParticipationListener {
        fun onParticipationClicked(participation: Participation)
    }
}