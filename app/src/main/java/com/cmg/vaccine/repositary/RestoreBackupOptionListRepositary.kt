package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.response.UpdateUUIDResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class RestoreBackupOptionListRepositary(
        private val api: MyApi,
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    fun getUserData():User{
        return database.getDao().getExistingUserData()
    }

    fun saveSubId(patientSubId:String){
        preferenceProvider.saveSubId(patientSubId)
    }

    fun getPatientSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun getLogin():LoginPin{
        return database.getDao().getLoginPin()
    }

    suspend fun updateUUID(subId: String,uuid:String): UpdateUUIDResponse {
        return apiRequest {
            api.updateUUID(subId,uuid)
        }
    }
}