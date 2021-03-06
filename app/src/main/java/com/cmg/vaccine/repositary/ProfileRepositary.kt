package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.PatientRegResponse
import com.cmg.vaccine.model.response.RemoveDependentResponse
import com.cmg.vaccine.model.response.UpdatePatientResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class ProfileRepositary(
        private val api: MyApi,
    private val database:AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun updateUser(user: User):Int{
        return database.getDao().updateUser(user)
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

    fun getDependent(subId: String):Dependent{
        return database.getDao().getDependent(subId)
    }

    fun getAllCountriesDB():List<Countries>{
        return database.getDao().getAllCountries()
    }

    /*suspend fun updateProfile(updateProfileReq: UpdateProfileReq): UpdatePatientResponse {
        return apiRequest {
            api.updateProfile(updateProfileReq)
        }
    }*/

    fun getMasterSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun saveEditProfileReq(updateProfileReq: UpdateProfileReq){
        preferenceProvider.saveEditProfileReq(updateProfileReq)
    }

    fun setProfileImage(uri:String){
        preferenceProvider.saveProfileImage(uri)
    }

    fun getProfileImage():String?{
        return database.getDao().getParentProfileImage(preferenceProvider.getSubId()!!)
    }
    fun getDependentProfileImage(subsId: String):String?{
        return database.getDao().getDependentProfileImage(subsId)
    }

    fun removeDependent(subId: String){
        database.getDao().deleteDependent(subId)
    }

    fun getTestReportList(privateKey: String):List<TestReport>{
        return database.getDao().getTestReportList(privateKey)
    }

    fun getAllIdentifierType():List<IdentifierType>{
        return database.getDao().getAllIdentifierType()
    }

    suspend fun removeDependentFromAPI(masterSubId:String,depSubId:String):RemoveDependentResponse{
        return apiRequest {
            api.removeDependent(masterSubId,depSubId)
        }
    }

    fun getNoOfDependentCount():String{
        return database.getDao().getNoOfDependentCount()
    }
}