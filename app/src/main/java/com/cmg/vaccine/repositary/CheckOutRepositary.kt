package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class CheckOutRepositary(
    private val database: AppDatabase,
    private val preference:PreferenceProvider
) {

    fun getUser():User{
        return database.getDao().getUserData(preference.getSubId()!!)
    }
}