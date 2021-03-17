package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class DepartureVerificationRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
) {

    fun getUserData(): User {
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }
}