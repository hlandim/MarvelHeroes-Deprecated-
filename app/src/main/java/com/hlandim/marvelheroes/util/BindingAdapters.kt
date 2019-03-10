@file:Suppress("DEPRECATION")

package com.hlandim.marvelheroes.util

import android.databinding.BindingAdapter
import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.model.Hero
import com.hlandim.marvelheroes.model.Participation
import com.hlandim.marvelheroes.view.details.ParticipationAdapter
import com.hlandim.marvelheroes.view.list.HeroesAdapter


@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, list: MutableList<Hero>) {
    recyclerView.adapter?.let {
        if (it is HeroesAdapter) {
            it.replaceItems(list)
        }
    }
}


@BindingAdapter("participation")
fun setParticipation(listView: ListView, list: List<Participation>) {
    val participation = list.take(3)
    listView.adapter?.let {
        if (it is ParticipationAdapter) {
            it.setParticipation(participation)
        }
    }

}


@BindingAdapter("textHtml")
fun setTextHtml(textView: TextView, textHtml: String) {

    val htmlAsSpanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(textHtml, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(textHtml)
    }

    textView.text = htmlAsSpanned
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    if (!TextUtils.isEmpty(url)) {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(view)
    } else {
        view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_image_placeholder))
    }
}

@BindingAdapter("imageResource")
fun setImageResource(fab: FloatingActionButton, resource: Int) {
    fab.setImageDrawable(ContextCompat.getDrawable(fab.context, resource))
}

