package com.hlandim.marvelheroes.util

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.model.HeroResponse
import com.hlandim.marvelheroes.view.HeroesAdapter

@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, list: MutableList<HeroResponse>) {
    recyclerView.adapter.let {
        if (it is HeroesAdapter) {
            it.replaceItems(list)
        }
    }
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
