package com.hlandim.marvelheroes.web

import android.app.Application
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxRetryCallAdapterFactory(val application: Application) : CallAdapter.Factory() {
    companion object {
        fun create(application: Application): CallAdapter.Factory {
            return RxRetryCallAdapterFactory(application)
        }
    }

    private val originalFactory = RxJava2CallAdapterFactory.create()

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val adapter = originalFactory.get(returnType, annotations, retrofit) ?: return null
        return RxRetryCallAdapter(adapter, application)
    }
}