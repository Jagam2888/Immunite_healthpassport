package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.prefernces.PreferenceProvider

class LoginPinRepositary(
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
) {

    fun insertPin(loginPin: LoginPin):Long{
        return database.getDao().insertLoginPin(loginPin)
    }

    fun getLoginPin():LoginPin{
        return database.getDao().getLoginPin()
    }

    fun updateLoginPin(loginPin: LoginPin):Int{
        return database.getDao().updateLoginPin(loginPin)
    }
}