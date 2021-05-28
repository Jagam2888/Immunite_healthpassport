package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.R
import com.cmg.vaccine.data.WorldEntryRuleData
import com.cmg.vaccine.data.WorldEntryRulePairResult
import com.cmg.vaccine.database.*
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.FlagModel
import com.cmg.vaccine.model.response.WorldEntriesCountryListData
import com.cmg.vaccine.repositary.WorldEntryRepositary
import com.cmg.vaccine.util.*
import com.google.gson.Gson
import io.paperdb.Paper
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

    val _vaccineList:MutableLiveData<List<VaccineReport>> = MutableLiveData()
    val vaccineList:LiveData<List<VaccineReport>>
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
    val fullName:MutableLiveData<String> = MutableLiveData()
    val privateKey:MutableLiveData<String> = MutableLiveData()

    init {
        val userData = Paper.book().read<Dashboard>(Passparams.CURRENT_USER_SUBSID,null)
        if (userData != null) {
            if (!userData.fullName.isNullOrEmpty()) {
                fullName.value = userData.fullName
                privateKey.value = userData.privateKey
            }
        }
    }

    fun loadAPIs() {
        getAllAirportCities()
        getAllWorldEntryCountryRules()
        //loadWorldEntryCountries()
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
                        repositary.deleteAllWorlEntryCountries()
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
                        //checkWorldEntriesAlreadySelected(getWorldEntryCountries)
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
            //checkWorldEntriesAlreadySelected(getWorldEntryCountries)
        }

    }

    private fun checkWorldEntriesAlreadySelected(countriesList:List<WorldEntryCountries>){
        if (!countriesList.isNullOrEmpty()) {
            val tempList = arrayListOf<WorldEntryCountries>()
            if (!worldEntriesList.value.isNullOrEmpty()) {
                /*countriesList.forEach {
                    if (!worldEntriesList.value?.contains(it.countryCodeAlpha)!!) {
                        tempList.add(it)
                    }
                }*/
                for (i in countriesList.indices){
                    for (j in worldEntriesList.value!!.indices){
                        if (countriesList[i].countryCodeAlpha != worldEntriesList.value!![j].countryCodeAlpha){
                            tempList.add(countriesList[i])
                        }
                    }
                }
            }else{
                tempList.addAll(countriesList)
            }
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
            listener?.onFailure("1Sorry! You already added this country")
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
        //val vaccineList = repositary.getVaccineReportList(repositary.getParentPrivateKey()!!)

        if (!privateKey.value.isNullOrEmpty()) {
            //val testReportList = repositary.getTestReportList(repositary.getParentPrivateKey()!!)
            val testReportList = repositary.getTestReportList(privateKey.value!!)
            _testReportList.value = testReportList

            val vaccineList = repositary.getVaccineReportList(privateKey.value!!)
            _vaccineList.value = vaccineList
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

        var testReportstatus = false
        var vaccineReportstatus = 2

        getWorldEntryRuleByCountry.forEach { worldEntryRulesByCountry ->
            when(worldEntryRulesByCountry.woen_rule_match_criteria){
                "A" ->{
                    entryRequirement.add(worldEntryRulesByCountry.woen_rule_description!!)
                }
                "P" ->{
                    personalData.add(worldEntryRulesByCountry.woen_rule_description!!)
                }
                "T" -> {

                    var successTestReport: String = ""
                    var title = worldEntryRulesByCountry.woen_rule_description
                    if (!testReportList.value.isNullOrEmpty()){
                        testReportList.value?.forEach { test ->
                            //if (worldEntryRulesByCountry.woen_test_code.equals(test.dateDisplayTitle)){
                            testReportstatus = validateTestReportWorldEntry(countryCode)
                            //title = test.NameDisplayTitle
                            val gson = Gson()
                            successTestReport = gson.toJson(test)
                            //}
                        }
                }
                    testReportArrayList.add("$title|$testReportstatus|$successTestReport")
                }
                "V" ->{
                    var successVaccine:String = ""
                    var title = worldEntryRulesByCountry.woen_rule_description
                    var vaccineCode:String = worldEntryRulesByCountry.woen_vaccine_code!!
                    if (!vaccineList.value.isNullOrEmpty()) {
                        vaccineList.value?.forEach { vaccine ->
                            //if (worldEntryRulesByCountry.woen_test_code.equals(vaccine.vaccineCode)){
                            vaccineReportstatus = validateVaccineReport(countryCode)
                            //title = vaccine.vaccineDisplayName
                            val gson = Gson()
                            successVaccine = gson.toJson(vaccine)
                            //}
                        }
                    }else{
                        vaccineReportstatus = 2
                    }
                    vaccineArrayList.add("$title|$vaccineReportstatus|$successVaccine|$vaccineCode")
                }
            }

        }

        expandableListDetail["Entry Requirements"] = entryRequirement
        expandableListDetail["Test|$testReportstatus"] = testReportArrayList
        expandableListDetail["Vaccine|$vaccineReportstatus"] = vaccineArrayList
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

    fun validateTestReportWorldEntry(countryCode: String):Boolean{
        var hours:Int = 0
        val worldEntryRule = repositary.getJoinWorldEntryRuleAndPriority(countryCode)
        var listTestReportFilterByHours:ArrayList<TestReport> = ArrayList()

        var observationCode = ArrayList<String>()
        var observationCodeMandatory = ArrayList<WorldEntryRuleData>()
        var observationCodeSelective = ArrayList<WorldEntryRuleData>()
        var priorRulePairList = ArrayList<String>()
        var worldEntryRulePairTotalResult = ArrayList<WorldEntryRulePairResult>()

        val testReport = repositary.getTestReportList(privateKey.value!!)
        worldEntryRule.forEach {
            when(it.woen_rule_match_criteria){
                "T" ->{
                    if (it.prioRuleCriteria.equals("Mandatory",false)) {
                        if (!it.woen_duration_hours.isNullOrEmpty()) {
                            hours = it.woen_duration_hours.toInt()
                        }
                        if (!it.woen_test_code.isNullOrEmpty()) {
                            observationCode.add(it.woen_test_code)
                        }
                        val worlEntryRuleData = WorldEntryRuleData(
                            hours,
                            it.woen_test_code,
                            it.prioRulePair,
                            it.prioRuleCriteria
                        )
                        observationCodeMandatory.add(worlEntryRuleData)
                    }
                    if (it.prioRuleCriteria.equals("Selective",false)) {
                        if (!it.woen_duration_hours.isNullOrEmpty()) {
                            hours = it.woen_duration_hours.toInt()
                        }
                        if (!it.woen_test_code.isNullOrEmpty()) {
                            observationCode.add(it.woen_test_code)
                        }
                        val worlEntryRuleData = WorldEntryRuleData(
                            hours,
                            it.woen_test_code,
                            it.prioRulePair,
                            it.prioRuleCriteria
                        )
                        observationCodeSelective.add(worlEntryRuleData)
                    }
                }
            }
        }

        val testReportFilterByTestCode = repositary.getTestReportFilterByTestCodes(privateKey.value!!,countryCode)
        if (testReportFilterByTestCode.isNullOrEmpty()){
            return false
        }else{
            if (observationCodeMandatory.size > 0){
                if (testReportFilterByTestCode.size >= observationCodeMandatory.size) {
                    for (i in observationCodeMandatory.indices) {
                        for (j in testReportFilterByTestCode.indices) {
                            if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())){
                                val sampleDate = changeDateFormatNewISO8601(testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00")
                                val calculateHours = calculateHours(System.currentTimeMillis(),changeDateToTimeStamp(sampleDate!!)!!)
                                if (calculateHours != null){
                                    if (calculateHours <= observationCodeMandatory[i].hours) {
                                        //listTestReportFilterByHours.add(it)
                                        if (i+1 == observationCodeMandatory.size){
                                            return true
                                        }
                                    }else{
                                        return false
                                    }

                                }
                            }
                        }
                    }
                }else{
                    return false
                }
            }
            if (observationCodeSelective.size > 0){
                observationCodeSelective.forEach {
                    if (it.priorRulePair.isNullOrEmpty()){
                        for (j in testReportFilterByTestCode.indices) {
                            if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())){
                                val sampleDate = changeDateFormatNewISO8601(testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00")
                                val calculateHours = calculateHours(System.currentTimeMillis(),changeDateToTimeStamp(sampleDate!!)!!)
                                if (calculateHours != null){
                                    //val result:Boolean =
                                    if (calculateHours <= it.hours){
                                        return true
                                    }
                                }
                            }
                        }
                    }else{
                        if (!priorRulePairList.contains(it.priorRulePair)){
                            priorRulePairList.add(it.priorRulePair!!)
                        }
                    }
                }
                if ((priorRulePairList.size > 0) and (priorRulePairList.size <= testReportFilterByTestCode.size)){
                    for (i in priorRulePairList.indices){
                        for (k in observationCodeSelective.indices){
                            if (observationCodeSelective[k].priorRulePair == priorRulePairList[i]){
                                for (j in testReportFilterByTestCode.indices) {
                                    if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())){
                                        val sampleDate = changeDateFormatNewISO8601(testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00")
                                        val calculateHours = calculateHours(System.currentTimeMillis(),changeDateToTimeStamp(sampleDate!!)!!)
                                        if (calculateHours != null){
                                            val rulePairResult = WorldEntryRulePairResult(
                                                priorRulePairList[i],
                                                calculateHours <= observationCodeSelective[k].hours
                                            )
                                            worldEntryRulePairTotalResult.add(rulePairResult)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (worldEntryRulePairTotalResult.size > 0){
                        var finalResult = ArrayList<Boolean>()
                        for (i in priorRulePairList.indices){


                            for (j in worldEntryRulePairTotalResult.indices){
                                if (worldEntryRulePairTotalResult[j].rulePair == priorRulePairList[i]){
                                    val rulePair = WorldEntryRulePairResult(
                                        priorRulePairList[i],
                                        true
                                    )
                                    if (finalResult.size < i+1) {
                                        if (worldEntryRulePairTotalResult.contains(rulePair)) {
                                            finalResult.add(true)
                                        } else {
                                            finalResult.add(false)
                                        }
                                    }
                                }
                            }
                        }
                        Log.d("priorrule",finalResult.toString())
                        return !finalResult.contains(false)
                    }
                }else{
                    return false
                }
            }else{
                return false
            }
            /*if (observationCodeSelective.size > 0){
                for (i in observationCodeSelective.indices){
                    if (observationCodeSelective[i].priorRulePair.isNullOrEmpty()) {
                        for (j in testReportFilterByTestCode.indices) {
                            if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())){
                                val sampleDate = changeDateFormatNewISO8601(testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00")
                                val calculateHours = calculateHours(System.currentTimeMillis(),changeDateToTimeStamp(sampleDate!!)!!)
                                if (calculateHours != null){
                                    return calculateHours <= observationCodeSelective[i].hours
                                }
                            }
                        }
                    }else{
                        var result = ArrayList<WorldEntryRulePairResult>()
                        var priorRulePairList = ArrayList<String>()
                        observationCodeSelective.forEach {
                            if (!priorRulePairList.contains(it.priorRulePair)){
                                priorRulePairList.add(it.priorRulePair!!)
                            }
                        }
                        *//*if (!priorRulePairList.contains(observationCodeSelective[i].priorRulePair)){
                            priorRulePairList.add(observationCodeSelective[i].priorRulePair!!)
                        }*//*

                        *//*if (testReportFilterByTestCode.size < priorRulePairList.size){
                            return false
                        }*//*

                        for (i in priorRulePairList.indices){
                            for (k in observationCodeSelective.indices){
                                if (observationCodeSelective[k].priorRulePair == priorRulePairList[i]){
                                    for (j in testReportFilterByTestCode.indices) {
                                        if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())){
                                            val sampleDate = changeDateFormatNewISO8601(testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00")
                                            val calculateHours = calculateHours(System.currentTimeMillis(),changeDateToTimeStamp(sampleDate!!)!!)
                                            if (calculateHours != null){
                                                val rulePairResult = WorldEntryRulePairResult(
                                                    priorRulePairList[i],
                                                    calculateHours <= observationCodeSelective[k].hours
                                                )
                                                result.add(rulePairResult)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (result.size > 0){
                            var finalResult = ArrayList<Boolean>()
                            for (i in priorRulePairList.indices){


                                for (j in result.indices){
                                    if (result[j].rulePair == priorRulePairList[i]){
                                        val rulePair = WorldEntryRulePairResult(
                                            priorRulePairList[i],
                                            true
                                        )
                                        if (finalResult.size < i+1) {
                                            if (result.contains(rulePair)) {
                                                finalResult.add(true)
                                            } else {
                                                finalResult.add(false)
                                            }
                                        }
                                    }
                                }
                            }
                        Log.d("priorrule",finalResult.toString())
                            return !finalResult.contains(false)
                        }
                    }
                }
            }*/
        }






     /*   testReport.forEach {
            if ((!it.dateSampleCollected.isNullOrEmpty()) and (!it.timeSampleCollected.isNullOrEmpty())){
                val sampleDate = changeDateFormatNewISO8601(it.dateSampleCollected + " " + it.timeSampleCollected + ":00")
                val calculateHours = calculateHours(System.currentTimeMillis(),changeDateToTimeStamp(sampleDate!!)!!)
                if (calculateHours != null){
                    if (calculateHours <= hours) {
                        listTestReportFilterByHours.add(it)
                    }

                }
            }
        }

        var testCodesFilterByTestReport = ArrayList<TestCodes>()

        val testCodes = repositary.getTestCodesByCategory(observationCode,countryCode)
        for (i in testCodes.indices){
            for (j in listTestReportFilterByHours.indices){
                if (testCodes[i].wetstTestCode.equals(listTestReportFilterByHours[j].testCode)){
                    testCodesFilterByTestReport.add(testCodes[i])
                }
            }
        }

        if ((!listTestReportFilterByHours.isNullOrEmpty()) and (!testCodesFilterByTestReport.isNullOrEmpty())){
            for (i in testCodesFilterByTestReport.indices){
                for (j in listTestReportFilterByHours.indices){
                    if (!listTestReportFilterByHours[j].observationCode.isNullOrEmpty()){
                        val observationStatusCode = testCodesFilterByTestReport[i].wetstObservationStatusCode
                        val observationStatusCodeArray = observationStatusCode?.split("|")
                        val observationTrimArray = ArrayList<String>()
                        for (k in observationStatusCodeArray!!.indices){
                            observationTrimArray.add(observationStatusCodeArray[k].trim())
                        }
                        if (observationTrimArray?.contains(listTestReportFilterByHours[j].observationCode!!)){
                            return true
                        }
                    }
                }
            }
        }*/

        return false
    }

    fun validateVaccineReport(countryCode: String):Int{

        // 0 means vaccine not inject but priority
        //1 means vaccine inject with priority
        //2 means vaccine not priority

        val worldEntryRulesVaccineByCountry = repositary.getWorldEntryRuleVaccineByCountry(countryCode)
        if (!worldEntryRulesVaccineByCountry.isNullOrEmpty()) {
            if (!vaccineList.value.isNullOrEmpty()) {
                for (i in worldEntryRulesVaccineByCountry.indices){
                    for (j in vaccineList.value!!.indices){
                        if (worldEntryRulesVaccineByCountry[i].woen_vaccine_code == vaccineList.value!![j].vaccineCode){
                            return 1
                        }
                    }
                }
                /*vaccineList.value!!.forEach {
                    if (worldEntryRulesVaccineByCountry.woen_vaccine_code.equals(it.vaccineCode)) {
                        return 1
                    }
                }*/
            }else{
                return 0
            }
        }else{
            return 2
        }
        return 0
    }


    fun getVaccineDetail(vaccineCode:String){
        val vaccineDetails = repositary.getVaccineDetail(vaccineCode)
        if (vaccineDetails != null){
            _vaccineDetail.value = vaccineDetails
        }
    }

}