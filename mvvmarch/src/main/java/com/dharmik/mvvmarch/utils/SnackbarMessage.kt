package com.dharmik.mvvmarch.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer

class SnackbarMessage : SingleLiveEvent<String>() {

    fun observe(owner: LifecycleOwner, observer: SnackbarObserver) {
        super.observe(owner, Observer {
            if (it == null) {
                return@Observer
            }

            observer.onNewMessage(it)
        })
    }

    interface SnackbarObserver {
        fun onNewMessage(message: String)
    }
}