package com.hlandim.marvelheroes.view.details

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.transition.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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
                TransitionInflater.from(context).inflateTransition(android.R.transition.move);
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
//        supportPostponeEnterTransition()

        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
//                supportStartPostponedEnterTransition()
                return false//To change body of created functions use File | Settings | File Templates.
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
//                supportStartPostponedEnterTransition()
                return false
            }
        }

        val requestOptions = RequestOptions.placeholderOf(R.drawable.ic_image_placeholder)
            .dontTransform().onlyRetrieveFromCache(true)
        Glide.with(binding.image.context)
            .load(imageUrl)
            .listener(listener)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .apply(requestOptions)
            .into(binding.image)

//        binding.posterImageView.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .addSharedElement(binding.image2, "heroImage2")
//                .addToBackStack(null)
//                .replace(R.id.fragment_hero_image, HeroImageFragment())
//                .commit()
//        }
    }

    class HeroImageFragmentTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform())
                .addTransition(ChangeImageTransform())
        }
    }
}