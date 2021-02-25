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
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.getCountryNameUsingCode
import com.cmg.vaccine.util.getWorldEntryCountryNameUsingCode
import com.google.gson.Gson
import java.lang.reflect.Array.get

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


    init {
        var getWorldEntryCountries = repositary.getWorldEntryCountries()
        if (getWorldEntryCountries.isNullOrEmpty()) {
            Couritnes.main {
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
                    "Y"
            )
            repositary.insertWorldEntry(addEntry)

            loadWorldEntriesData()
        }else{
            listener?.onFailure("Sorry! You already added this country")
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
        val testReportList = repositary.getTestReportList()
        _testReportList.value = testReportList
    }

    /*val _worldEntriesData:MutableLiveData<LinkedHashMap<String, List<String>>>{

    }*/

    fun loadEntriesExpandableListData(){

        val userData = repositary.getUserData()

        val expandableListDetail =
                LinkedHashMap<String, List<String>>()

        val entryRequirement: MutableList<String> =
                ArrayList()
        entryRequirement.add("API not ready")

        val testReportArrayList = ArrayList<String>()
        val vaccineArrayList = ArrayList<String>()

        if (vaccineList.value?.isNotEmpty() == true) {
            vaccineList.value?.forEach { vaccine ->

                //why i need to pass entire data, currently don't have id for vaccine and testreport from API
                val gson = Gson()
                val value: String = gson.toJson(vaccine)
                vaccineArrayList.add(value)
            }
        }

        if (testReportList.value?.isNotEmpty() == true) {
            testReportList.value?.forEach { testReport ->
                //why i need to pass entire data, currently don't have id for vaccine and testreport from API
                val gson = Gson()
                val value: String = gson.toJson(testReport)
                testReportArrayList.add(value)
            }
        }

        val personalData = ArrayList<String>()


        personalData.add(getWorldEntryCountryNameUsingCode(userData.nationality,countryList.value!!)!!)

        /*var colorIndicator = "red"
        if ((vaccineList.value?.isEmpty() == true) and (testReportList.value?.isEmpty() == true)){
            colorIndicator = "red"
        }else if ((vaccineList.value?.isNotEmpty() == true) and (testReportList.value?.isNotEmpty() == true)){
            colorIndicator = "green"
        }else if ((vaccineList.value?.isNotEmpty() == true) and (testReportList.value?.isEmpty() == true)){
            colorIndicator = "green"
        }else if ((vaccineList.value?.isEmpty() == true) and (testReportList.value?.isNotEmpty() == true)){
            colorIndicator = "yellow"
        }*/

        expandableListDetail["Entry Requirements"] = entryRequirement
        expandableListDetail["Test"] = testReportArrayList
        expandableListDetail["Vaccine"] = vaccineArrayList
        expandableListDetail["Personal Profile Details"] = personalData

        _worldEntriesExpandableData.value = expandableListDetail

    }

}