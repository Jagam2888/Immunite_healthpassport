package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class HomeRepositary(
    private val api: MyApi,
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    suspend fun getVaccineInfo(privateKey:String): VaccineResponse {
        return apiRequest {
            api.searchPatientVaccine(privateKey)
        }
    }

    suspend fun getVaccineList(subsId:String):VaccineListResponse{
        return apiRequest {
            api.searchVaccineList(subsId)
        }
    }

    suspend fun updatePrivateKeyStatus(subId: String,status:String):UpdateUUIDResponse{
        return apiRequest {
            api.updatePrivateKeyStatus(subId,status)
        }
    }

    /*suspend fun getTestReportList(subsId:String):TestReportListResponse{
        return apiRequest {
            api.searchTestReportList(subsId)
        }
    }*/

    suspend fun getTestReportListFromAPI(privateKey: String):TestReportResponseBlockChain{
        return apiRequest {
            api.getTestReportList(privateKey)
        }
    }
    suspend fun getVaccineListNew(privateKey: String):VaccineResponseBlockChain{
        return apiRequest {
            api.getVaccineList(privateKey)
        }
    }


    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun getParentPrivateKey():String?{
        return database.getDao().getPrivateKey(preferenceProvider.getSubId()!!)
    }

    fun getDependentList():List<Dependent>?{
        return database.getDao().getDependentList(preferenceProvider.getSubId()!!)
    }

    fun getSubsId():String?{
        return preferenceProvider.getSubId()
    }

    fun insertVaccine(vaccine: Vaccine){
        database.getDao().insertVaccineData(vaccine)
    }

    fun insertTestReport(testReport: TestReport){
        database.getDao().insertTestReport(testReport)
    }

    fun getVaccineList():List<Vaccine>{
        return database.getDao().getVaccineList()
    }

    fun getTestReportList(privateKey: String):List<TestReport>{
        return database.getDao().getTestReportList(privateKey)
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

    fun getDependentData(subId: String):Dependent{
        return database.getDao().getDependent(subId)
    }

    fun updateDependent(dependent: Dependent):Int{
        return database.getDao().updateDependent(dependent)
    }

}