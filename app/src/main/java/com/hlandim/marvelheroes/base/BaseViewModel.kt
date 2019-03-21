package com.hlandim.marvelheroes.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.hlandim.marvelheroes.R
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.UnknownHostException

abstract class BaseViewModel(application: Application) :
    AndroidViewModel(application) {

    protected val compositeDisposable = CompositeDisposable()
    val messageEvent = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun handleCommunicationError(t: Throwable) {
        val message = when (t) {
            is UnknownHostException,
            is IOException -> getApplication<Application>().getString(R.string.network_error)
            is SocketException -> getApplication<Application>().getString(R.string.network_error)
            is HttpException -> getApplication<Application>().getString(R.string.invalid_parameters_error)
            else -> getApplication<Application>().getString(R.string.unknown_error)
        }
        messageEvent.value = message
    }
}