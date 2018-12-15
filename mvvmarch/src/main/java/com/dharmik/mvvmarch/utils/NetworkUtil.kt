package com.dharmik.mvvmarch.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkUtil private constructor(private val activity: Activity) {
    private val TAG = NetworkUtil::class.java.name

    val isConnectedToNetwork: Boolean
        get() {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var networkUtil: NetworkUtil? = null

        fun getInstance(activity: Activity): NetworkUtil {

            networkUtil = NetworkUtil(activity)

            return networkUtil as NetworkUtil
        }
    }

}