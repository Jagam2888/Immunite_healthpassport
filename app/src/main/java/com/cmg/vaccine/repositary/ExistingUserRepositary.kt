package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.response.*
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

    fun getPatientSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun deleteOldUser(){
        database.getDao().deleteOldUser()
    }

    fun getUserCount():Int{
        return database.getDao().getUserCount()
    }

    suspend fun getWorldEntriesCountryList(): WorldEntriesCountryList {
        return apiRequest {
            api.getWorldEntriesCountryList()
        }
    }

    suspend fun updateUUID(subId: String,uuid:String):UpdateUUIDResponse{
        return apiRequest {
            api.updateUUID(subId,uuid)
        }
    }

    suspend fun updateFCMToken(subId: String,token:String):UpdateFCMTokenResponse{
        return apiRequest {
            api.updateFCMToken(subId,token)
        }
    }

    fun insertWorldEntryCountries(worldEntryCountries: WorldEntryCountries){
        database.getDao().insertWorldCountries(worldEntryCountries)
    }

    fun getWorldEntryCountries():List<WorldEntryCountries>{
        return database.getDao().getAllWorldCountries()
    }

    suspend fun getIdentifierTypeFromAPI(): IdentifierTypeResponse {
        return apiRequest {
            api.getIdentifierType()
        }
    }
    fun insertIdentifierType(identifierType: IdentifierType){
        database.getDao().insertIdentifierType(identifierType)
    }
}