package com.code93.linkcoop.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission

object ConnectivityUtils {
    /**
     * To get ConnectivityManager
     *
     * @param mContext Context to get ConnectivityManager
     * @return ConnectivityManager
     */
    @RequiresPermission(allOf = ["android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"])
    private fun getConnectivityManager(mContext: Context): ConnectivityManager {
        return mContext.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * To check whether internet data is on
     *
     * @param mContext Context to get ConnectivityManager
     * @return true if internet connection is on or false otherwise
     */
    @JvmStatic
    @RequiresPermission(allOf = ["android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"])
    fun isInternetOn(mContext: Context): Boolean {
        val activeNetwork = getConnectivityManager(mContext).activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    /**
     * To check whether wifi data is on
     *
     * @param mContext Context to get ConnectivityManager
     * @return true if internet connection is wifi or false otherwise
     */
    @JvmStatic
    @RequiresPermission(allOf = ["android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"])
    fun isWiFiOn(mContext: Context): Boolean {
        val activeNetwork = getConnectivityManager(mContext).activeNetworkInfo
        return activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * To check whether mobile data is on
     *
     * @param mContext Context to get ConnectivityManager
     * @return true if internet connection is mobile data or false otherwise
     */
    @JvmStatic
    @RequiresPermission(allOf = ["android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"])
    fun isMobileDataOn(mContext: Context): Boolean {
        val activeNetwork = getConnectivityManager(mContext).activeNetworkInfo
        return activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_MOBILE
    }
}