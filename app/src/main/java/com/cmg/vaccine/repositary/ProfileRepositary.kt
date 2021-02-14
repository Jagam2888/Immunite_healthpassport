package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.PatientRegResponse
import com.cmg.vaccine.model.response.UpdatePatientResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class ProfileRepositary(
        private val api: MyApi,
    private val database:AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    fun getUserData(verifyStatus:String):User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!,verifyStatus)
    }

    fun getUserEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun saveUser(user: User){
        preferenceProvider.saveUserReqData(user)
    }

    fun getDependentList(privateKey:String):List<Dependent>{
        return database.getDao().getDependentList(privateKey)
    }

    fun getDependent(privateKey: String):Dependent{
        return database.getDao().getDependent(privateKey)
    }

    fun getAllCountriesDB():List<Countries>{
        return database.getDao().getAllCountries()
    }

    suspend fun updateProfile(updateProfileReq: UpdateProfileReq): UpdatePatientResponse {
        return apiRequest {
            api.updateProfile(updateProfileReq)
        }
    }
}