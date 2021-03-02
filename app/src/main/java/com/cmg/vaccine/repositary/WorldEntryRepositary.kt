package com.cmg.vaccine.repositary

import com.cmg.vaccine.data.WorldEntriesListData
import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class WorldEntryRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider,
    private val api: MyApi
):SafeAPIRequest() {

    fun getWorldEntryCountries():List<WorldEntryCountries>{
        return database.getDao().getAllWorldCountries()
    }

    fun insertWorldEntryCountries(worldEntryCountries: WorldEntryCountries){
        database.getDao().insertWorldCountries(worldEntryCountries)
    }

    fun insertWorldEntry(addWorldEntries: AddWorldEntries){
        database.getDao().insertAddWorldEntry(addWorldEntries)
    }

    fun getWorldEntriesList():List<AddWorldEntries>{
        return database.getDao().getWorldEntries()
    }

    fun deleteCountry(countryName: String){
        database.getDao().deleteAddWorldEntries(countryName)
    }

    fun countryExists(countryName:String):Int{
        return database.getDao().getCountryExists(countryName)
    }

    fun getVaccineList():List<Vaccine>{
        return database.getDao().getVaccineList()
    }

    fun getTestReportList():List<TestReport>{
        return database.getDao().getTestReportList()
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }
    suspend fun getWorldEntriesCountryList():WorldEntriesCountryList{
        return apiRequest {
            api.getWorldEntriesCountryList()
        }
    }

    fun insertWorldEntryRuleByCountry(worldEntryRulesByCountry: WorldEntryRulesByCountry){
        database.getDao().insertWorldEntryRulesByCountry(worldEntryRulesByCountry)
    }

    fun getWorldEntryRulesByCountry(countryCode:String):List<WorldEntryRulesByCountry>{
        return database.getDao().getWorldEntryRuleByCountry(countryCode)
    }

    suspend fun getWorldEntryRulesByCountryFromAPI(countryCode: String):WorldEntryRulesResponse{
        return apiRequest {
            api.getWorldEntryCountryRules(countryCode)
        }
    }

    suspend fun getVaccineDetailListFromAPI():VaccineDetailListResponse{
        return apiRequest {
            api.getVaccineDetailList()
        }
    }

    suspend fun getTestTypeDataListFromAPI():TestTypeResponse{
        return apiRequest {
            api.getTestType()
        }
    }
    fun insertVaccineDetail(vaccineDetail: VaccineDetail){
        database.getDao().insertVaccineDetail(vaccineDetail)
    }

    fun getVaccineDetailList():List<VaccineDetail>{
        return database.getDao().getVaccineDetailList()
    }

    fun insertTestType(testType: TestType){
        database.getDao().insertTestType(testType)
    }

    fun getTestTypeList():List<TestType>{
        return database.getDao().getTestTypeList()
    }

    fun getVaccineDetail(vaccineCode:String):VaccineDetail{
        return database.getDao().getVaccineDetail(vaccineCode)
    }
}