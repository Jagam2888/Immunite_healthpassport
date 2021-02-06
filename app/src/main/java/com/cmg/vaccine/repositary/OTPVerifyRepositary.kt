package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class OTPVerifyRepositary(
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
) {

    fun getUserData(email:String,verifyStatus:String):User{
        return database.getDao().getUserData(email,verifyStatus)
    }

    fun updateVerifyStatus(user: User):Int{
        return database.getDao().updateVerifyStatus(user)
    }

    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun updateUser(user: User):Int{
        return database.getDao().updateUser(user)
    }

    fun getUserData():String?{
        return preferenceProvider.getUserReqData()
    }
}