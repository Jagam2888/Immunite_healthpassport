package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.prefernces.PreferenceProvider

class SettingsRepositary(
        private val database: AppDatabase,
) {

    fun updateLoginPin(loginPin: LoginPin):Int{
        return database.getDao().updateLoginPin(loginPin)
    }

    fun getLoginPin():LoginPin{
        return database.getDao().getLoginPin()
    }
}