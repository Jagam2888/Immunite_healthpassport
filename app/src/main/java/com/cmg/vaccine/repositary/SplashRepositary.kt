package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class SplashRepositary(
    private val preferenceProvider: PreferenceProvider,
    private val database: AppDatabase
) {

    fun getUserData(email:String,verifyStatus:String):User{
        return database.getDao().getUserData(email,verifyStatus)
    }

    fun getLoginPin():LoginPin{
        return database.getDao().getLoginPin()
    }

    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }
    fun getURL() : String? {
        return preferenceProvider.getURL()
    }

    fun addURL(url:String){
        preferenceProvider.saveURL(url)
    }
}