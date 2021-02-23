package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.network.BlockChainAPi
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

    /*suspend fun getTestReportList(subsId:String):TestReportListResponse{
        return apiRequest {
            api.searchTestReportList(subsId)
        }
    }*/

    suspend fun getTestReportList(privateKey: String):TestReportNewResponse{
        return apiRequest {
            api.getTestReportList(privateKey)
        }
    }
    suspend fun getVaccineListNew(privateKey: String):VaccineNewResponse{
        return apiRequest {
            api.getVaccineList(privateKey)
        }
    }


    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!,"Y")
    }

    fun getPrivateKey():String?{
        return preferenceProvider.getPrincipalPrivateKey()
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