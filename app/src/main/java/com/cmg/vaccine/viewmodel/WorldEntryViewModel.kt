package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.R
import com.cmg.vaccine.database.*
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.FlagModel
import com.cmg.vaccine.model.response.WorldEntriesCountryListData
import com.cmg.vaccine.repositary.WorldEntryRepositary
import com.cmg.vaccine.util.*
import com.google.gson.Gson
import java.lang.Exception
import java.lang.reflect.Array.get
import java.net.SocketTimeoutException

class WorldEntryViewModel(
    private val repositary:WorldEntryRepositary
):ViewModel() {

    /*val _countryList:MutableLiveData<ArrayList<Countries>> = MutableLiveData()
    val countryList:LiveData<ArrayList<Countries>>
    get() = _countryList*/

    val _countryList:MutableLiveData<ArrayList<WorldEntryCountries>> = MutableLiveData()
    val countryList:LiveData<ArrayList<WorldEntryCountries>>
        get() = _countryList

    var _worldEntriesList:MutableLiveData<ArrayList<AddWorldEntries>> = MutableLiveData()
    val worldEntriesList:LiveData<ArrayList<AddWorldEntries>>
    get() = _worldEntriesList

    val _vaccineList:MutableLiveData<List<Vaccine>> = MutableLiveData()
    val vaccineList:LiveData<List<Vaccine>>
    get() = _vaccineList

    val _testReportList:MutableLiveData<List<TestReport>> = MutableLiveData()
    val testReportList:LiveData<List<TestReport>>
        get() = _testReportList

    var listener:SimpleListener?=null

    val _selectedCountryName:MutableLiveData<String> = MutableLiveData()
    val selectedCountryName:LiveData<String>
    get() = _selectedCountryName

    private var _worldEntriesExpandableData:MutableLiveData<LinkedHashMap<String,List<String>>> = MutableLiveData()

    val worldEntriesExpandable:LiveData<LinkedHashMap<String,List<String>>>
    get() = _worldEntriesExpandableData

    var _worldEntryRuleByCountry:MutableLiveData<List<WorldEntryRulesByCountry>> = MutableLiveData()

    val worldEntryRulesByCountry:LiveData<List<WorldEntryRulesByCountry>>
    get() = _worldEntryRuleByCountry

    var _vaccineDetailList:MutableLiveData<List<VaccineDetail>> = MutableLiveData()
    val vaccineDetailList:LiveData<List<VaccineDetail>>
    get() = _vaccineDetailList

    var _testTypeList:MutableLiveData<List<TestType>> = MutableLiveData()
    val testTypeList:LiveData<List<TestType>>
    get() = _testTypeList

    var _vaccineDetail:MutableLiveData<VaccineDetail> = MutableLiveData()
    val vaccineDetail:LiveData<VaccineDetail>
        get() = _vaccineDetail

    fun loadAPIs() {
        getAllAirportCities()
        getAllWorldEntryCountryRules()
        loadWorldEntryCountries()
    }

    private fun getAllAirportCities(){
        val getAllAirportCitiesDB = repositary.getAllAirportCities()
        if (getAllAirportCitiesDB.isNullOrEmpty()) {
            Couritnes.main {
                try {
                    val getAllAirportCities = repositary.getAllAirportCitiesFromAPI()
                    if ((getAllAirportCities != null) and (getAllAirportCities.data.isNotEmpty())) {
                        getAllAirportCities.data.forEach {
                            val airportCitiesName = AirportCitiesName(
                                    it.airportName,
                                    it.cityCode,
                                    it.countryCode,
                                    it.countryName,
                                    it.id
                            )
                            repositary.insertAirportCitiesMaster(airportCitiesName)
                        }
                    }
                    //getWorldEntryCountries()
                } catch (e: APIException) {
                    listener?.onShowToast(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure("3"+e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onShowToast(e.message!!)
                } catch (e: Exception) {
                    listener?.onShowToast(e.message!!)
                }

            }
        }
    }

    private fun getAllWorldEntryCountryRules(){
        val getAllWorldEntries = repositary.getAllWorldEntryRulesByCountry()
        if (getAllWorldEntries.isNullOrEmpty()) {
            Couritnes.main {
                try {
                    val worlentryRules = repositary.getAllWorldEntryCountryRulesFromAPI()
                    if (!worlentryRules.data.isNullOrEmpty()) {
                        worlentryRules.data.forEach { data ->
                            val worldEntryRulesByCountry = WorldEntryRulesByCountry(
                                    data.woenSeqNo,
                                    data.woenCountryCode,
                                    data.woenDurationHours,
                                    data.woenEnddate,
                                    data.woenPoints,
                                    data.woenRuleDescription,
                                    data.woenRuleMatchCriteria,
                                    data.woenRuleSeqNo,
                                    data.woenStartdate,
                                    data.woenStatus,
                                    data.woenTestCode,
                                    data.woenVaccineCode

                            )
                            repositary.insertWorldEntryRuleByCountry(worldEntryRulesByCountry)
                        }
                    }
                    //getAllAirportCities()
                } catch (e: APIException) {
                    listener?.onShowToast(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure("3"+e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onShowToast(e.message!!)
                } catch (e: Exception) {
                    listener?.onShowToast(e.message!!)
                }
            }
        }
    }


    fun loadWorldEntryCountries() {
        var getWorldEntryCountries = repositary.getWorldEntryCountries()
        if (getWorldEntryCountries.isNullOrEmpty()) {
            listener?.onStarted()
            Couritnes.main {
                try {
                    val worldEntryCountries = repositary.getWorldEntriesCountryList()
                    if (!worldEntryCountries.data.isNullOrEmpty()) {
                        worldEntryCountries.data.forEach { data->
                            val worldEntryCountries = WorldEntryCountries(
                                    data.countryName,
                                    data.countryCodeAlpha,
                                    data.countryMstrSeqno
                            )
                            repositary.insertWorldEntryCountries(worldEntryCountries)
                        }
                        getWorldEntryCountries = repositary.getWorldEntryCountries()
                        val tempList = arrayListOf<WorldEntryCountries>()
                        tempList.addAll(getWorldEntryCountries)
                        _countryList.value = tempList
                        listener?.onSuccess("")
                    }
                }catch (e:Exception){
                    listener?.onShowToast(e.message!!)
                }catch (e:NoInternetException){
                    listener?.onFailure("3"+e.message!!)
                }catch (e:SocketTimeoutException){
                    listener?.onShowToast(e.message!!)
                }

            }
        }else{
           val tempList = arrayListOf<WorldEntryCountries>()
           tempList.addAll(getWorldEntryCountries)
           _countryList.value = tempList
        }

    }

    fun insertWorldEntry(country:String,countryCode:String){
        val countryExists = repositary.countryExists(country)
        if (countryExists == 0) {
            val addEntry = AddWorldEntries(
                    country,
                    countryCode,
                    "G",
                    "Y",
                repositary.getCurrentCount()
            )
            repositary.insertWorldEntry(addEntry)

            loadWorldEntriesData()
        }else{
            listener?.onShowToast("Sorry! You already added this country")
        }
    }

    fun loadWorldEntriesData(){
        val entriesList = repositary.getWorldEntriesList()
        val convertArrayList = ArrayList<AddWorldEntries>(entriesList)
        //if (!entriesList.isNullOrEmpty()){
            _worldEntriesList.value = convertArrayList
        //}
    }

    fun deleteWorldEntries(countryName:String){
        repositary.deleteCountry(countryName)
        loadWorldEntriesData()
    }

    fun getVaccineAndTestReportList(){
        val vaccineList = repositary.getVaccineList()
        _vaccineList.value = vaccineList
        if (!repositary.getParentPrivateKey().isNullOrEmpty()) {
            val testReportList = repositary.getTestReportList(repositary.getParentPrivateKey()!!)
            _testReportList.value = testReportList
        }
    }

    /*val _worldEntriesData:MutableLiveData<LinkedHashMap<String, List<String>>>{

    }*/

    fun loadWorldEntryRulesByCountryData(countryCode: String){
        var getWorldEntryRuleByCountry = repositary.getWorldEntryRulesByCountry(countryCode)
        if (getWorldEntryRuleByCountry.isNullOrEmpty()){
            listener?.onStarted()
            Couritnes.main {
                try {
                    val response = repositary.getWorldEntryRulesByCountryFromAPI(countryCode)
                    if (!response.data.isNullOrEmpty()){
                        response.data.forEach {data->
                            val worldEntryRulesByCountry = WorldEntryRulesByCountry(
                                    data.woenSeqNo,
                                    data.woenCountryCode,
                                    data.woenDurationHours,
                                    data.woenEnddate,
                                    data.woenPoints,
                                    data.woenRuleDescription,
                                    data.woenRuleMatchCriteria,
                                    data.woenRuleSeqNo,
                                    data.woenStartdate,
                                    data.woenStatus,
                                    data.woenTestCode,
                                    data.woenVaccineCode

                            )
                            repositary.insertWorldEntryRuleByCountry(worldEntryRulesByCountry)
                        }

                        getWorldEntryRuleByCountry = repositary.getWorldEntryRulesByCountry(countryCode)
                        _worldEntryRuleByCountry.value = getWorldEntryRuleByCountry
                        loadEntriesExpandableListData(countryCode)
                        loadVaccineDataList()
                        //listener?.onSuccess("")
                    }else{
                        listener?.onShowToast("No Rules for this country")
                    }
                }catch (e:Exception){
                    listener?.onShowToast(e.message!!)
                }catch (e:NoInternetException){
                    listener?.onFailure("3"+e.message!!)
                }catch (e:SocketTimeoutException){
                    listener?.onShowToast(e.message!!)
                }
            }
        }else{
            _worldEntryRuleByCountry.value = getWorldEntryRuleByCountry
            loadEntriesExpandableListData(countryCode)
            loadVaccineDataList()
        }
    }

    private fun loadEntriesExpandableListData(countryCode:String){

        var getWorldEntryRuleByCountry = repositary.getWorldEntryRulesByCountry(countryCode)

        val expandableListDetail =
                LinkedHashMap<String, List<String>>()

        val entryRequirement: MutableList<String> =
                ArrayList()
        val testReportArrayList = ArrayList<String>()
        val vaccineArrayList = ArrayList<String>()
        val personalData = ArrayList<String>()

        getWorldEntryRuleByCountry.forEach { worldEntryRulesByCountry ->
            when(worldEntryRulesByCountry.woen_rule_match_criteria){
                "A" ->{
                    entryRequirement.add(worldEntryRulesByCountry.woen_rule_description!!)
                }
                "P" ->{
                    personalData.add(worldEntryRulesByCountry.woen_rule_description!!)
                }
                "T" ->{
                    var status = "false"
                    var successTestReport:String = ""
                    testReportList.value?.forEach { test->
                        if (worldEntryRulesByCountry.woen_test_code.equals(test.dateDisplayTitle)){
                            status = "true"
                            val gson = Gson()
                            successTestReport = gson.toJson(test)
                        }
                    }
                    testReportArrayList.add(worldEntryRulesByCountry.woen_rule_description!!+"|"+status+"|"+successTestReport)
                }
                "V" ->{
                    var status = "false"
                    var successVaccine:String = ""
                    var vaccineCode:String = worldEntryRulesByCountry.woen_vaccine_code!!
                    vaccineList.value?.forEach { vaccine ->
                        if (worldEntryRulesByCountry.woen_test_code.equals(vaccine.vaccinetype)){
                            status = "true"
                            val gson = Gson()
                            successVaccine = gson.toJson(vaccine)
                        }
                    }
                    vaccineArrayList.add(worldEntryRulesByCountry.woen_rule_description!!+"|"+status+"|"+successVaccine+"|"+vaccineCode)
                }
            }

        }

        expandableListDetail["Entry Requirements"] = entryRequirement
        expandableListDetail["Test"] = testReportArrayList
        expandableListDetail["Vaccine"] = vaccineArrayList
        expandableListDetail["Personal Profile Details"] = personalData

        _worldEntriesExpandableData.value = expandableListDetail

    }

    private fun loadVaccineDataList(){
        var getVaccineDetail = repositary.getVaccineDetailList()
        if (getVaccineDetail.isNullOrEmpty()) {
            listener?.onStarted()
            Couritnes.main {
                try {
                    val response = repositary.getVaccineDetailListFromAPI()
                    if (!response.data.isNullOrEmpty()) {
                        response.data.forEach { vaccineData ->
                            val vaccineDetail = VaccineDetail(
                                    vaccineData.loinc,
                                    vaccineData.snomed,
                                    vaccineData.vaccineSeqno,
                                    vaccineData.vaccine_code,
                                    vaccineData.vaccine_created_by,
                                    vaccineData.vaccine_created_date,
                                    vaccineData.vaccine_duration_between_dosses,
                                    vaccineData.vaccine_manufacterer,
                                    vaccineData.vaccine_manufacturing_year,
                                    vaccineData.vaccine_name,
                                    vaccineData.vaccine_no_of_days_for_maximum_efficacy,
                                    vaccineData.vaccine_no_of_doses,
                                    vaccineData.vaccine_status,
                                    vaccineData.vaccine_updated_by,
                                    vaccineData.vaccine_updated_date,
                                    vaccineData.vaccine_virus_code

                            )
                            repositary.insertVaccineDetail(vaccineDetail)

                        }
                        loadTestTypeList()
                        getVaccineDetail = repositary.getVaccineDetailList()
                        _vaccineDetailList.value = getVaccineDetail
                    }
                } catch (e: Exception) {
                    listener?.onShowToast(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure("3"+e.message!!)
                }
            }
        }else{
            getVaccineDetail = repositary.getVaccineDetailList()
            _vaccineDetailList.value = getVaccineDetail
            loadTestTypeList()
        }
    }

    private fun loadTestTypeList(){
        var getTestTypeList = repositary.getTestTypeList()
        if (getTestTypeList.isNullOrEmpty()) {
            listener?.onStarted()
            Couritnes.main {
                try {
                    val response = repositary.getTestTypeDataListFromAPI()
                    if (!response.data.isNullOrEmpty()) {
                        response.data.forEach {
                            val testType = TestType(
                                    it.loinc,
                                    it.snomed,
                                    it.testTypeSeqno,
                                    it.test_code,
                                    it.test_created_by,
                                    it.test_created_date,
                                    it.test_name,
                                    it.test_result_validaty,
                                    it.test_specimen,
                                    it.test_status,
                                    it.test_update_date,
                                    it.test_updated_by,
                                    it.test_virus_code
                            )
                            repositary.insertTestType(testType)
                        }
                        getTestTypeList = repositary.getTestTypeList()
                        _testTypeList.value = getTestTypeList
                        listener?.onSuccess("")
                    }
                } catch (e: Exception) {
                    listener?.onShowToast(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure("3"+e.message!!)
                }
            }
        }else{
            getTestTypeList = repositary.getTestTypeList()
            _testTypeList.value = getTestTypeList
            listener?.onSuccess("")
        }
    }

    fun changeOrder(countryName:String,newOrder:Int)
    {
        repositary.updateWEOrder(countryName,newOrder)

    }


    fun getVaccineDetail(vaccineCode:String){
        val vaccineDetails = repositary.getVaccineDetail(vaccineCode)
        if (vaccineDetails != null){
            _vaccineDetail.value = vaccineDetails
        }
    }

}