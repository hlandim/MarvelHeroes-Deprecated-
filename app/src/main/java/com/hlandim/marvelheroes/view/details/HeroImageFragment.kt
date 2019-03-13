package com.hlandim.marvelheroes.view.details

import android.os.Build
import android.os.Bundle
import android.support.transition.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.databinding.HeroImageFragmentBinding

class HeroImageFragment : Fragment() {

    companion object {
        fun newInstance(imageUrl: String): HeroImageFragment {
            return HeroImageFragment().apply {
                arguments = Bundle().apply {
                    putString("imageUrl", imageUrl)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = HeroImageFragmentBinding.inflate(inflater, container, false)
        arguments?.let {
            it.getString("imageUrl")?.let { imageUrl ->
                loadImage(binding, imageUrl)
            }

        }

        return binding.root
    }

    private fun loadImage(binding: HeroImageFragmentBinding, imageUrl: String) {

        val requestOptions = RequestOptions.placeholderOf(R.drawable.ic_image_placeholder)
            .dontTransform().onlyRetrieveFromCache(true)
        Glide.with(binding.image.context)
            .load(imageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .apply(requestOptions)
            .into(binding.image)

    }

    class HeroImageFragmentTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform())
                .addTransition(ChangeImageTransform())
        }
    }
}