package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.network.BlockChainAPi
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class SettingsRepositary(
    private val database: AppDatabase,
    private val api: MyApi,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    fun getSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun updateLoginPin(loginPin: LoginPin):Int{
        return database.getDao().updateLoginPin(loginPin)
    }

    fun getLoginPin():LoginPin{
        return database.getDao().getLoginPin()
    }

    fun deleteVaccine(){
        database.getDao().deleteAllVaccine()
    }

    fun deleteTestReport(){
        database.getDao().deleteAllTestReport()
    }

    fun insertVaccine(vaccine: Vaccine){
        database.getDao().insertVaccineData(vaccine)
    }

    fun insertTestReport(testReport: TestReport){
        database.getDao().insertTestReport(testReport)
    }

    suspend fun getVaccineList(subsId:String): VaccineListResponse {
        return apiRequest {
            api.searchVaccineList(subsId)
        }
    }

    suspend fun getVaccineListBlockChain(privateKey: String): VaccineNewResponse {
        return apiRequest {
            api.getVaccineList(privateKey)
        }
    }

    suspend fun getTestReportList(privateKey: String): TestReportNewResponse {
        return apiRequest {
            api.getTestReportList(privateKey)
        }
    }

    fun getPrivateKey():String?{
        return preferenceProvider.getPrincipalPrivateKey()
    }

    /*suspend fun getTestReportList(subsId:String): TestReportListResponse {
        return apiRequest {
            api.searchTestReportList(subsId)
        }
    }*/
}