package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
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

    fun getParentPrivateKey():String?{
        return database.getDao().getPrivateKey(preferenceProvider.getSubId()!!)
    }

    fun getDependentPrivateKey(subId: String):String?{
        return database.getDao().getDependentPrivateKey(subId)
    }

    fun savePrivateKey(privateKey:String){
        return preferenceProvider.savePrincipalPrivateKey(privateKey)
    }

    suspend fun getParentPrivateKeyFromAPI():GetPrivateKeyResponse{
        return apiRequest {
            api.getParentPrivateKey(preferenceProvider.getSubId()!!)
        }
    }

    suspend fun getDependentPrivateKeyFromAPI(subId:String):GetPrivateKeyResponse{
        return apiRequest {
            api.getDependentPrivateKey(subId)
        }
    }

    fun updateUser(user: User):Int{
        return database.getDao().updateUser(user)
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun getDependentData(subId: String):Dependent{
        return database.getDao().getDependent(subId)
    }

    fun updateDependent(dependent: Dependent):Int{
        return database.getDao().updateDependent(dependent)
    }
}