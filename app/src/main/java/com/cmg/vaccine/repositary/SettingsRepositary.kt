package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider
import okhttp3.ResponseBody

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

    suspend fun getVaccineListBlockChain(privateKey: String): VaccineResponseBlockChain {
        return apiRequest {
            api.getVaccineList(privateKey)
        }
    }

    suspend fun getTestReportList(privateKey: String): TestReportResponseBlockChain {
        return apiRequest {
            api.getTestReportList(privateKey)
        }
    }

    fun getPrivateKey():String?{
        return database.getDao().getPrivateKey(preferenceProvider.getSubId()!!)
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    suspend fun getVaccineTestRef(privateKey:String):ResponseBody{
        return apiRequest {
            api.getVaccineTestRef(privateKey)
        }
    }

    fun getAllDependent():List<Dependent>{
        return database.getDao().getDependentList(preferenceProvider.getSubId()!!)
    }

    fun insertWorldEntryCountries(worldEntryCountries: WorldEntryCountries){
        database.getDao().insertWorldCountries(worldEntryCountries)
    }

    fun deleteWorldEntryCountries(){
        database.getDao().deleteAllWorldCountries()
    }

    suspend fun getWorldEntriesCountryList():WorldEntriesCountryList{
        return apiRequest {
            api.getWorldEntriesCountryList()
        }
    }

    suspend fun getAllAirportCities():GetAllAirportCitiesResponse{
        return apiRequest {
            api.getAllAirportCities()
        }
    }

    suspend fun getAllWorldEntryCountryRules():WorldEntryRulesResponse{
        return apiRequest {
            api.getAllWorldEntryCountryRules()
        }
    }

    fun insertWorldEntryRuleByCountry(worldEntryRulesByCountry: WorldEntryRulesByCountry){
        database.getDao().insertWorldEntryRulesByCountry(worldEntryRulesByCountry)
    }

    fun deleteAllWorldEntryRuleByCountry(){
        database.getDao().deleteAllWorldEntryRuleByCountry()
    }

    fun insertAirportCitiesMaster(airportCitiesName: AirportCitiesName){
        database.getDao().insertAirportCitiesName(airportCitiesName)
    }

    fun deleteAllAirportCities(){
        database.getDao().deleteAllAirportCities()
    }

    suspend fun getIdentifierTypeFromAPI():IdentifierTypeResponse{
        return apiRequest {
            api.getIdentifierType()
        }
    }
    fun insertIdentifierType(identifierType: IdentifierType){
        database.getDao().insertIdentifierType(identifierType)
    }

    fun deleteIdentifierType(){
        database.getDao().deleteAllIdentifierType()
    }

    suspend fun getTestCodeFromApi():GetTestCodeResponse{
        return apiRequest {
            api.getTestCodes()
        }
    }
    fun insertTestCodes(testCodes: TestCodes){
        database.getDao().insertTestCodes(testCodes)
    }
    fun deleteTestCodes(){
        database.getDao().deleteAllTestCodes()
    }

    suspend fun getWorldPriority():GetWorldPriorityListResponse{
        return apiRequest {
            api.getWorldPriorityList()
        }
    }
    fun insertWorldPriority(worldPriority: WorldPriority){
        database.getDao().insertWorldPriority(worldPriority)
    }

    fun deleteWorldPriority(){
        database.getDao().deleteAllWorldPriority()
    }

    suspend fun getCountriesFromAPI():CountryResponse{
        return apiRequest {
            api.getAllCountries()
        }
    }

    fun insertCountries(countries: Countries){
        database.getDao().insertCountries(countries)
    }

    fun deleteAllCountries(){
        database.getDao().deleteAllCountries()
    }
}