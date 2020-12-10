package com.cmg.vaccine.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class CheckInternetConnectivity {
    companion object{
        const val errorMsg = "No Internet Connection";


        fun isAvailable(context:Context) : Boolean {
            var results = false
            val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            cm?.let {
                it.getNetworkCapabilities(cm.activeNetwork)?.apply {
                    results = when{
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                }
            }

            /*cm?.apply {
                val netInfo = activeNetworkInfo

                return netInfo != null && netInfo.isConnectedOrConnecting*/
            }
            return results
        }
    }
}