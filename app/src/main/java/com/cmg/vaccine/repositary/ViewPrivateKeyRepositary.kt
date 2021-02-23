package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.response.GetPrivateKeyResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class ViewPrivateKeyRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider,
    private val api: MyApi
):SafeAPIRequest() {

    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getPrivateKey():String?{
        return database.getDao().getPrivateKey(preferenceProvider.getSubId()!!)
    }

    fun savePrivateKey(privateKey:String){
        return preferenceProvider.savePrincipalPrivateKey(privateKey)
    }

    suspend fun getPrivateKeyFromAPI():GetPrivateKeyResponse{
        return apiRequest {
            api.getPrivateKey(preferenceProvider.getSubId()!!)
        }
    }

    fun updateUser(user: User):Int{
        return database.getDao().updateUser(user)
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!,"Y")
    }
}