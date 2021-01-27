package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class ProfileRepositary(
    private val database:AppDatabase,
    private val preferenceProvider: PreferenceProvider
) {

    fun getUserData(email:String):User{
        return database.getDao().getUserData(email)
    }

    fun getUserEmail():String?{
        return preferenceProvider.getEmail()
    }
}