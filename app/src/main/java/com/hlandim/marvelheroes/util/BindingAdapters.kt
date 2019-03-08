package com.hlandim.marvelheroes.util

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
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

//@BindingAdapter("posterImageUrl")
//fun setPosterImageUrl(view: ImageView, url: String?) {
//    if (!TextUtils.isEmpty(url)) {
//        Glide.with(view.context)
//            .load(MovieImageUrlBuilder.buildPosterUrl(url!!))
//            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
//            .into(view)
//    } else {
//        view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_image_placeholder))
//    }
//}
//
//@BindingAdapter("backdropImageUrl")
//fun setBackdropImageUrl(view: ImageView, url: String?) {
//    if (!TextUtils.isEmpty(url)) {
//        Glide.with(view.context)
//            .load(MovieImageUrlBuilder.buildBackdropUrl(url!!))
//            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
//            .into(view)
//    } else {
//        view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_image_placeholder))
//    }
//}
