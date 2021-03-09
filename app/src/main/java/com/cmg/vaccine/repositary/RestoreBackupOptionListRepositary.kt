package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class RestoreBackupOptionListRepositary(
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
) {

    fun getUserData():User{
        return database.getDao().getExistingUserData()
    }

    fun saveSubId(patientSubId:String){
        preferenceProvider.saveSubId(patientSubId)
    }

    fun getLogin():LoginPin{
        return database.getDao().getLoginPin()
    }
}