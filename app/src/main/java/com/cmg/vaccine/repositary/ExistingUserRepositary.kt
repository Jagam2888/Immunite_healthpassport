package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.response.GetExistingUserResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider
import okhttp3.ResponseBody

class ExistingUserRepositary(
    private val api: MyApi,
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {


    suspend fun getExistingUser(privateKey:String):ResponseBody{
        return apiRequest {
            api.getExistingUser(privateKey)
        }
    }

    suspend fun insertUserLocalDb(user: User){
        database.getDao().insertSignUp(user)
    }

    fun savePatientSubId(subId:String){
        preferenceProvider.saveSubId(subId)
    }

    fun deleteOldUser(){
        database.getDao().deleteOldUser()
    }

    fun getUserCount():Int{
        return database.getDao().getUserCount()
    }
}