package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.prefernces.PreferenceProvider

class ViewPrivateKeyRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
) {

    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getPrivateKey(email:String):String?{
        return database.getDao().getPrivateKey(email)
    }
}