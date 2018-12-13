package com.dharmik.mvvmarch.base.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.*
import android.content.Context
import com.dharmik.mvvmarch.utils.SnackbarMessage


abstract class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    private var uc: UIChangeLiveData? = null
    open val snackbarMessage = SnackbarMessage()

    fun showSnackbar(message: String) {
        snackbarMessage.value = message
    }

    fun showSnackbarBackground(message: String) {
        snackbarMessage.postValue(message)
    }

    protected fun getAppContext(): Context = getApplication<Application>().applicationContext

    protected fun <T> setObserverValue(observer: MutableLiveData<T>, data: T) {
        observer.value = data
    }

    fun getUC(): UIChangeLiveData {
        if (uc == null) {
            uc = UIChangeLiveData()
        }
        return uc as UIChangeLiveData
    }

    inner class UIChangeLiveData : LiveData<Any>() {
        private var showProgress: MutableLiveData<Any>? = null//String
        private var hideProgress: MutableLiveData<Any>? = null//Boolean

        fun getShowProgressLiveData(): MutableLiveData<Boolean> {
            showProgress = createLiveData(showProgress)
            return showProgress as MutableLiveData<Boolean>
        }

        fun getHideProgressLiveData(): MutableLiveData<Boolean> {
            hideProgress = createLiveData(hideProgress)
            return hideProgress as MutableLiveData<Boolean>
        }

        private fun createLiveData(liveData: MutableLiveData<Any>?): MutableLiveData<Any> {
            var data = liveData
            if (data == null) {
                data = MutableLiveData()
            }
            return data
        }
    }

    /**
     * An {@link Event Event} constant that can be used to match all events.
     * */
    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStart() {

    }

}