package com.mohitb117.stonks.common

import android.content.Context
import android.net.ConnectivityManager

fun Context.isNetworkAvailable(): Boolean {
    return try {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
    } catch (throwable: Throwable) {
        false
    }
}
