package com.cmg.vaccine.repositary

import com.cmg.vaccine.data.WorldEntriesListData
import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.JoinWorldEntryRuleAndPriority
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

    fun deleteAllWorlEntryCountries(){
        database.getDao().deleteAllWorldCountries()
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

    fun getTestReportList(privateKey:String):List<TestReport>{
        return database.getDao().getTestReportList(privateKey)
    }

    fun getParentPrivateKey():String?{
        return database.getDao().getPrivateKey(preferenceProvider.getSubId()!!)
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
        return database.getDao().getWorldEntryRuleByCountryByCode(countryCode)
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

    fun updateWEOrder(countryName:String,order:Int)
    {
        database.getDao().updateWEOrder(countryName,order)
    }

    fun getCurrentCount():Int{
        return database.getDao().getCurrentCount()
    }

    suspend fun getAllAirportCitiesFromAPI():GetAllAirportCitiesResponse{
        return apiRequest {
            api.getAllAirportCities()
        }
    }

    fun insertAirportCitiesMaster(airportCitiesName: AirportCitiesName){
        database.getDao().insertAirportCitiesName(airportCitiesName)
    }

    fun getAllAirportCities():List<AirportCitiesName>{
        return database.getDao().getAllAirportCities()
    }

    suspend fun getAllWorldEntryCountryRulesFromAPI():WorldEntryRulesResponse{
        return apiRequest {
            api.getAllWorldEntryCountryRules()
        }
    }

    fun getAllWorldEntryRulesByCountry():List<WorldEntryRulesByCountry>{
        return database.getDao().getAllWorldEntryRuleByCountry()
    }

    fun getJoinWorldEntryRuleAndPriority(countryCode: String):List<JoinWorldEntryRuleAndPriority>{
        return database.getDao().getJoinWorldEntryRuleAndPriority(countryCode)
    }

    fun getTestCodesByCategory(category:ArrayList<String>,countryCode: String):List<TestCodes>{
        return database.getDao().getAllTestCodesByCategory(category,countryCode)
    }


}