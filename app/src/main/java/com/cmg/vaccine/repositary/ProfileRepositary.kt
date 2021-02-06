package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

class ProfileRepositary(
    private val database:AppDatabase,
    private val preferenceProvider: PreferenceProvider
) {

    fun getUserData(email:String,verifyStatus:String):User{
        return database.getDao().getUserData(email,verifyStatus)
    }

    fun getUserEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun saveUser(user: User){
        preferenceProvider.saveUserReqData(user)
    }

    fun getDependentList(privateKey:String):List<Dependent>{
        return database.getDao().getDependentList(privateKey)
    }
}