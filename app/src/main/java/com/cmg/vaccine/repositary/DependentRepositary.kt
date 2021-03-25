package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.DependentRegResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider
import okhttp3.ResponseBody

class DependentRepositary(
    private val api: MyApi,
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    suspend fun dependentSignUp(dependentRegReq: DependentRegReq):DependentRegResponse{
        return apiRequest {
            api.dependentSignUp(dependentRegReq)
        }
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    suspend fun insertDependentSignUp(dependent: Dependent):Long{
        return database.getDao().insertDependent(dependent)
    }

    fun getDependent(subsId:String):Dependent{
        return database.getDao().getDependent(subsId)
    }

    fun updateDependent(dependent: Dependent):Int{
        return database.getDao().updateDependent(dependent)
    }

    fun getAllCountriesDB():List<Countries>{
        return database.getDao().getAllCountries()
    }

    /*suspend fun updateDependentProfile(updateProfileReq: UpdateProfileReq):DependentRegResponse{
        return apiRequest {
            api.updateDependentProfile(updateProfileReq)
        }
    }*/

    fun saveEditProfileReq(updateProfileReq: UpdateProfileReq){
        preferenceProvider.saveEditProfileReq(updateProfileReq)
    }

    /*fun saveDependentReq(dependentRegReq: DependentRegReq){
        preferenceProvider.saveaddDependentReq(dependentRegReq)
    }*/

    fun getParentSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun getProfileImage(subsId: String):String?{
        return database.getDao().getDependentProfileImage(subsId)
    }

    suspend fun getExistingUser(privateKey:String): ResponseBody {
        return apiRequest {
            api.getExistingUser(privateKey)
        }
    }
    fun getTestReportList(privateKey: String):List<TestReport>{
        return database.getDao().getTestReportList(privateKey)
    }
    fun getAllIdentifierType():List<IdentifierType>{
        return database.getDao().getAllIdentifierType()
    }
}