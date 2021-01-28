package com.cmg.vaccine.repositary

import androidx.lifecycle.LiveData
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.model.response.VaccineListResponse
import com.cmg.vaccine.model.response.ViewReport
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class ViewReportListRepositary(
        private val api: MyApi,
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

   suspend fun getVaccineList(privateKey:String):VaccineListResponse{
       return apiRequest {
           api.searchVaccineList(privateKey)
       }
   }

    fun getPrivateKey():String?{
        return database.getDao().getPrivateKey(preferenceProvider.getEmail()!!)
    }
}