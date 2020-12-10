package com.cmg.vaccine.repositary

import com.cmg.vaccine.prefernces.PreferenceProvider

class SplashRepositary(
    private val preferenceProvider: PreferenceProvider
) {

    fun getURL() : String? {
        return preferenceProvider.getURL()
    }

    fun addURL(url:String){
        preferenceProvider.saveURL(url)
    }
}