package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.IdentifierType
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.response.IdentifierTypeResponse
import com.cmg.vaccine.model.response.PatientRegResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class TellUsRepositary(
    private val api: MyApi,
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    suspend fun signUp(signUpReq: SignUpReq): PatientRegResponse {
        return apiRequest {
            api.signUp(signUpReq)
        }
    }

    suspend fun insertUser(user: User){
        database.getDao().insertSignUp(user)
    }

    fun getUserData():String?{
        return preferenceProvider.getUserReqData()
    }

    fun saveUserEmail(email:String){
        preferenceProvider.saveEmail(email)
    }

    fun saveUserSubId(subId:String){
        preferenceProvider.saveSubId(subId)
    }

    fun getAllCountriesDB():List<Countries>{
        return database.getDao().getAllCountries()
    }

    fun getFCMToken():String?{
        return preferenceProvider.getFCMTOKEN()
    }

    suspend fun getIdentifierTypeFromAPI(): IdentifierTypeResponse {
        return apiRequest {
            api.getIdentifierType()
        }
    }
    fun insertIdentifierType(identifierType: IdentifierType){
        database.getDao().insertIdentifierType(identifierType)
    }

    fun getAllIdentifierType():List<IdentifierType>{
        return database.getDao().getAllIdentifierType()
    }
}