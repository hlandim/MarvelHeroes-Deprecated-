package com.hlandim.marvelheroes.view.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.hlandim.marvelheroes.databinding.FragmentParticipationDetailsBinding
import com.hlandim.marvelheroes.database.model.Participation
import com.hlandim.marvelheroes.viewmodel.HeroViewModel
import kotlinx.android.synthetic.main.fragment_participation_details.*

class ParticipationFragment : Fragment() {

    private lateinit var mParticipation: Participation

    companion object {
        fun newInstance(participation: Participation): ParticipationFragment {
            return ParticipationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("mParticipation", participation)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mParticipation = arguments?.getSerializable("mParticipation") as Participation
        val viewModel = ViewModelProviders.of(this.activity!!).get(HeroViewModel::class.java)
        viewModel.getParticipationDetails(mParticipation.resourceURI)
        val binding = FragmentParticipationDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overlay.visibility = View.INVISIBLE
        content.visibility = View.INVISIBLE
        btnClose.setOnClickListener { startCloseAnimation() }
        overlay.setOnClickListener { startCloseAnimation() }
        startInitialAnimation()
    }

    private fun startInitialAnimation() {
        YoYo.with(Techniques.FadeIn).onEnd {
            overlay.visibility = View.VISIBLE
            YoYo.with(Techniques.SlideInUp).onStart { content.visibility = View.VISIBLE }.duration(200).playOn(content)
        }.duration(200).playOn(overlay)
    }

    fun startCloseAnimation() {
        if (isAdded && activity != null) {
            YoYo.with(Techniques.SlideOutDown).onEnd {
                content.visibility = View.INVISIBLE
                YoYo.with(Techniques.FadeOut).onEnd {
                    overlay.visibility = View.INVISIBLE
                    activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
                }.duration(200).playOn(overlay)
            }.duration(200).playOn(content)
        }
    }


}