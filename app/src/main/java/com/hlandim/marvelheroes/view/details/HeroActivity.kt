package com.hlandim.marvelheroes.view.details

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionSet
import android.view.ViewTreeObserver
import android.widget.ListView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.database.model.Hero
import com.hlandim.marvelheroes.database.model.Participation
import com.hlandim.marvelheroes.database.model.Thumbnail
import com.hlandim.marvelheroes.databinding.ActivityHeroBinding
import com.hlandim.marvelheroes.util.getViewModel
import com.hlandim.marvelheroes.viewmodel.HeroViewModel
import com.hlandim.marvelheroes.web.ResultParticipationResponse
import kotlinx.android.synthetic.main.activity_hero.*

class HeroActivity : AppCompatActivity(), ParticipationAdapter.ParticipationListener {


    lateinit var mViewModel: HeroViewModel

    companion object {
        const val FRAGMENT_PARTICIPATION_TAG = "participation_fragment"
        const val FRAGMENT_IMAGE_TAG = "image_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        val binding = DataBindingUtil.setContentView<ActivityHeroBinding>(this, R.layout.activity_hero)
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


        binding.heroContent.listComics.adapter = ParticipationAdapter(emptyList(), this)
        binding.heroContent.listEvents.adapter = ParticipationAdapter(emptyList(), this)
        binding.heroContent.listSeries.adapter = ParticipationAdapter(emptyList(), this)
        binding.heroContent.listStories.adapter = ParticipationAdapter(emptyList(), this)

        configureListHeight(binding.heroContent.listComics)
        configureListHeight(binding.heroContent.listEvents)
        configureListHeight(binding.heroContent.listSeries)
        configureListHeight(binding.heroContent.listStories)
    }

    private fun configureListHeight(list: ListView) {
        val viewTreeObserver = list.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    list.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val layoutParams = list.layoutParams
                    val finalHeight = list.height * list.adapter.count * 1.3
                    layoutParams.height = finalHeight.toInt()
                    list.layoutParams = layoutParams
                }
            })
        }
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
