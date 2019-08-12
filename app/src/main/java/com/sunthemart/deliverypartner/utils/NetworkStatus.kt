package com.sunthemart.deliverypartner.view.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * network status class
 */
class NetworkStatus(context: Context) {
    private var mContext: Context? = context

    /**
     * checking network available or not
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info)
            if (i.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        return false
    }

}