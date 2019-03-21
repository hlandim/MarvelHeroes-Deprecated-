package com.hlandim.marvelheroes.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.hlandim.marvelheroes.BuildConfig
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.web.mavel.MarvelApi

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity(), BaseActionListener {

    companion object {
        const val ERROR_ACTION = "${BuildConfig.APPLICATION_ID}.action.ERROR"
    }

    lateinit var defaultErrorMsg: String
    protected abstract val layoutId: Int
    protected lateinit var binding: B
    private var snackbar: Snackbar? = null
    private val chainErrorReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ERROR_ACTION) {
                if (snackbar == null) {
                    var msg = defaultErrorMsg
                    intent.extras?.let {
                        msg = it.getString("message", defaultErrorMsg)
                    }
                    snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.try_again)) {
                            snackbar = null
                            MarvelApi.subject.onNext(Unit)
                        }
                    snackbar?.show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultErrorMsg = getString(R.string.unknown_error)
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    override fun onDisplayMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ERROR_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(chainErrorReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chainErrorReceiver)
    }

}