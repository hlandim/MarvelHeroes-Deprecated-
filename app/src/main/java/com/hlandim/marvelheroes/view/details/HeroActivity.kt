package com.hlandim.marvelheroes.view.details

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.base.BaseActivity
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.database.model.Participation
import com.hlandim.marvelheroes.database.model.Thumbnail
import com.hlandim.marvelheroes.databinding.ActivityHeroBinding
import com.hlandim.marvelheroes.util.getViewModel
import com.hlandim.marvelheroes.view.details.HeroImageFragment.HeroImageFragmentTransition
import com.hlandim.marvelheroes.viewmodel.HeroViewModel
import com.hlandim.marvelheroes.web.ResultParticipationResponse
import kotlinx.android.synthetic.main.activity_hero.*


class HeroActivity : BaseActivity<ActivityHeroBinding>(),
    ParticipationAdapter.ParticipationListener {

    override val layoutId: Int
        get() = R.layout.activity_hero

    lateinit var mViewModel: HeroViewModel

    companion object {
        const val FRAGMENT_PARTICIPATION_TAG = "participation_fragment"
        const val FRAGMENT_IMAGE_TAG = "image_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        var hero = intent?.extras?.getParcelable("hero") as Hero
        savedInstanceState?.let {
            hero = it.getParcelable("hero") as Hero
        }
        mViewModel = createViewModel()

        mViewModel.hero.value = hero

        binding.lifecycleOwner = this
        this.lifecycle.addObserver(mViewModel)
        binding.viewModel = mViewModel

        window.sharedElementEnterTransition = TransitionSet()
            .addTransition(ChangeImageTransform())
            .addTransition(ChangeBounds())
            .apply {
                loadImage(binding, hero)
            }

        binding.heroContent.listParticipation.setAdapter(ParticipationAdapter(emptyList(), this))

        mViewModel.messageEvent.observe(this, Observer {
            it?.let { message ->
                onDisplayMessage(message)
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable("hero", mViewModel.hero.value)
        super.onSaveInstanceState(outState)
    }

    override fun onParticipationClicked(participation: Participation) {
        mViewModel.participation.value = ResultParticipationResponse(
            participation.name, "",
            Thumbnail(0, "", "")
        )
        val detailsFragment = ParticipationFragment.newInstance(participation)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_details, detailsFragment, FRAGMENT_PARTICIPATION_TAG).commit()
    }


    private fun loadImage(binding: ActivityHeroBinding, hero: Hero) {
        supportPostponeEnterTransition()

        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                supportStartPostponedEnterTransition()
                return false//To change body of created functions use File | Settings | File Templates.
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                supportStartPostponedEnterTransition()
                return false
            }
        }

        val requestOptions = RequestOptions.placeholderOf(R.drawable.ic_image_placeholder)
            .dontTransform().onlyRetrieveFromCache(true)
        Glide.with(binding.posterImageView.context)
            .load(hero.thumbnail.getFullThumbnailUrl())
            .listener(listener)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .apply(requestOptions)
            .into(binding.posterImageView)

        configureImageClick(hero, binding)
    }

    private fun configureImageClick(
        hero: Hero,
        binding: ActivityHeroBinding
    ) {
        val fragment = HeroImageFragment.newInstance(hero.thumbnail.getFullThumbnailUrl())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.sharedElementEnterTransition = HeroImageFragmentTransition()
            fragment.enterTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.fade)
            fragment.sharedElementReturnTransition = HeroImageFragmentTransition()
        }

        binding.posterImageView.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .addSharedElement(it, "heroImage")
                .replace(R.id.fragment_hero_image, fragment, FRAGMENT_IMAGE_TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun createViewModel(): HeroViewModel {
        return getViewModel { HeroViewModel(application) }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_PARTICIPATION_TAG)
        if (fragment != null) {
            (fragment as ParticipationFragment).startCloseAnimation()
        } else {
            val position = intent?.extras?.getInt("position")
            val bundle = Bundle().apply {
                position?.let {
                    putInt("position", position)
                }
                putParcelable("hero", mViewModel.hero.value)
            }
            val intent = Intent().apply {
                putExtras(bundle)
            }
            setResult(Activity.RESULT_OK, intent)
            super.onBackPressed()
        }
    }
}
