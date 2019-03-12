package com.arctouch.codechallenge.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Shortcut of Schedulers.io()
 * @return the result of Schedulers.trampoline()
 * @see Schedulers.io()
 */
fun ioThread(): Scheduler {
    return Schedulers.trampoline()
}

/**
 * Shortcut of AndroidSchedulers.mainThread()
 * @return the result of Schedulers.trampoline()
 * @see AndroidSchedulers.mainThread()
 */
fun androidThread(): Scheduler {
    return Schedulers.trampoline()
}