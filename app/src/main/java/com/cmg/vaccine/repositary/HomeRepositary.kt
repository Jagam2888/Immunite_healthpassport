package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.response.TestReportListResponse
import com.cmg.vaccine.model.response.VaccineListResponse
import com.cmg.vaccine.model.response.VaccineResponse
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

    suspend fun getTestReportList(subsId:String):TestReportListResponse{
        return apiRequest {
            api.searchTestReportList(subsId)
        }
    }

    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!,"Y")
    }

    fun getPrivateKey():String?{
        return database.getDao().getPrivateKey(preferenceProvider.getEmail()!!)
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

    fun getTestReportList():List<TestReport>{
        return database.getDao().getTestReportList()
    }
}