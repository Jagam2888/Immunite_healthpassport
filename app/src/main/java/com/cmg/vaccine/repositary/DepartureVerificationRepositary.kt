package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.JoinWorldEntryRuleAndPriority
import com.cmg.vaccine.model.TestCodeFilterByReport
import com.cmg.vaccine.model.request.WebCheckInReq
import com.cmg.vaccine.model.response.WebCheckInResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class DepartureVerificationRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider,
    private val api:MyApi
):SafeAPIRequest() {

    fun getUserData(): User {
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun getWorldEnteryRuleByCountry(countryCode:String,criteria:String):List<WorldEntryRulesByCountry>{
        return database.getDao().getWorldEntryRuleForMAS(countryCode,criteria)
    }

    fun getAirportCityByCode(cityCode:String): AirportCitiesName {
        return database.getDao().getAirportCityByCode(cityCode)
    }

    fun getTestReportList(privateKey:String):List<TestReport>{
        return database.getDao().getTestReportList(privateKey)
    }

    fun getWorldPriorityByRuleNo(ruleNo:String,countryCode: String):WorldPriority{
        return database.getDao().getAllWorldPriorityByRuleNo(ruleNo,countryCode)
    }

    fun getTestCodesByCategory(category:ArrayList<String>,countryCode: String):List<TestCodes>{
        return database.getDao().getAllTestCodesByCategory(category,countryCode)
    }

    fun getTestReportByTestCode(testCode:String):TestReport{
        return database.getDao().getTestReportByTestCode(testCode)
    }

    fun getMaxHoursFromWorldEntryRulePriority(countryCode:String,criteria:String,ruleSeqno:String):String{
        return database.getDao().getMaxHoursWorldEntryRuleForMAS(countryCode,criteria,ruleSeqno)
    }

    fun getJoinWorldEntryRuleAndPriority(countryCode: String):List<JoinWorldEntryRuleAndPriority>{
        return database.getDao().getJoinWorldEntryRuleAndPriority(countryCode)
    }

    fun getTestReportFilterByTestCodes(privateKey: String,countryCode: String):List<TestReport>{
        return database.getDao().gettestReportFilterByTestCodes(privateKey,countryCode)
    }

    fun getFilterTestCodeByReport(privateKey: String,countryCode: String):List<TestCodeFilterByReport>{
        return database.getDao().getFilterTestCodeByReport(privateKey,countryCode)
    }

    fun getCounterCheckinDecryptKey(mapKey:String):String{
        return database.getDao().getCounterCheckinDecryptKey(mapKey)
    }

    suspend fun webCheckInApi(webCheckInReq: WebCheckInReq):WebCheckInResponse{
        return apiRequest {
            api.webCheckInAPI(webCheckInReq)
        }
    }

}