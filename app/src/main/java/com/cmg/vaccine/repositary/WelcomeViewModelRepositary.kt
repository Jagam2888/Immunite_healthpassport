package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.model.response.SystemConfigResponse
import com.cmg.vaccine.model.response.SystemConfigResponseData
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class WelcomeViewModelRepositary(
        private val api: MyApi,
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    suspend fun getSystemConfigDataFromAPI(): SystemConfigResponse {
        return apiRequest {
            api.getSystemConfigData()
        }
    }

    fun insertSystemConfigData(systemConfigResponseData: SystemConfigResponseData){
        database.getDao().insertSystemConfig(systemConfigResponseData)
    }

    fun saveNotificationStatus(){
        preferenceProvider.saveNotificationSoundStatus(true)
    }
}