package com.hlandim.marvelheroes.web

import android.app.Application
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.hlandim.marvelheroes.base.BaseActivity
import com.hlandim.marvelheroes.web.mavel.MarvelApi
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type
import java.net.UnknownHostException

class RxRetryCallAdapter<R>(private val originalAdapter: CallAdapter<R, *>, val application: Application) :
    CallAdapter<R, Any> {

    override fun adapt(call: Call<R>): Any {
        val adaptedValue = originalAdapter.adapt(call)
        return when (adaptedValue) {
            is Completable -> {
                adaptedValue
                    .doOnError(this::sendBroadcast)
                    .retryWhen {
                        retryFlowable()
                    }
            }
            is Single<*> -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen {
                        retryFlowable()
                    }
            }
            is Maybe<*> -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen {
                        retryFlowable()
                    }
            }
            is Observable<*> -> {
                adaptedValue
                    .doOnError(this::sendBroadcast)
                    .retryWhen {
                        MarvelApi.subject
                            .observeOn(Schedulers.io())
                    }
            }
            is Flowable<*> -> {
                adaptedValue.doOnError(this::sendBroadcast)
                    .retryWhen {
                        retryFlowable()
                    }
            }
            else -> {
                adaptedValue
            }
        }
    }

    private fun retryFlowable() = MarvelApi.subject.toFlowable(BackpressureStrategy.LATEST)
        .observeOn(Schedulers.io())


    override fun responseType(): Type = originalAdapter.responseType()

    private fun sendBroadcast(t: Throwable) {
        val message = when (t) {
            is UnknownHostException,
            is IOException -> application.getString(com.hlandim.marvelheroes.R.string.network_error)
            is HttpException -> application.getString(com.hlandim.marvelheroes.R.string.invalid_parameters_error)
            else -> application.getString(com.hlandim.marvelheroes.R.string.unknown_error)
        }
        val intent = Intent(BaseActivity.ERROR_ACTION).apply { putExtra("message", message) }
        LocalBroadcastManager.getInstance(application).sendBroadcast(intent)
    }
}