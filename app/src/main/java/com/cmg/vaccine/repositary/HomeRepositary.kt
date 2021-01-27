package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class HomeRepositary(
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
) {

    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getEmail()!!)
    }
}