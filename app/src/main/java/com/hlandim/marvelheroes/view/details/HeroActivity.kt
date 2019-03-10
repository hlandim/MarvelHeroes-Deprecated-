package com.hlandim.marvelheroes.view.details

import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import com.hlandim.marvelheroes.databinding.ActivityHeroBinding
import com.hlandim.marvelheroes.model.Hero
import com.hlandim.marvelheroes.model.Participation
import com.hlandim.marvelheroes.model.ResultParticipationResponse
import com.hlandim.marvelheroes.model.Thumbnail
import com.hlandim.marvelheroes.util.getViewModel
import com.hlandim.marvelheroes.viewmodel.HeroViewModel
import com.hlandim.marvelheroes.web.mavel.HeroesRepository
import com.hlandim.marvelheroes.web.mavel.HeroesService
import com.hlandim.marvelheroes.web.mavel.MarvelApi
import kotlinx.android.synthetic.main.activity_hero.*

class HeroActivity : AppCompatActivity(), ParticipationAdapter.ParticipationListener {


    lateinit var mViewModel: HeroViewModel

    companion object {
        const val FRAGMENT_TAG = "participation_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        val binding = DataBindingUtil.setContentView<ActivityHeroBinding>(this, R.layout.activity_hero)
        val hero = intent?.extras?.getSerializable("hero") as Hero
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


        binding.heroContent.listComics.adapter = ParticipationAdapter(emptyList(), this)
        binding.heroContent.listEvents.adapter = ParticipationAdapter(emptyList(), this)
        binding.heroContent.listSeries.adapter = ParticipationAdapter(emptyList(), this)
        binding.heroContent.listStories.adapter = ParticipationAdapter(emptyList(), this)

    }

    override fun onParticipationClicked(participation: Participation) {
        mViewModel.participation.value = ResultParticipationResponse(participation.name, "", Thumbnail("", ""))
        val detailsFragment = ParticipationFragment.newInstance(participation)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_details, detailsFragment, FRAGMENT_TAG).commit()
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
    }


    private fun createViewModel(): HeroViewModel {
        val heroesService = HeroesService(MarvelApi.create())
        val heroesRepository = HeroesRepository(heroesService)
        return getViewModel { HeroViewModel(application, heroesRepository) }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment != null) {
            (fragment as ParticipationFragment).startCloseAnimation()
        } else {
            super.onBackPressed()
        }
    }
}
