package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.AddWorldEntries
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.WorldEntryRepositary

class WorldEntryViewModel(
    private val repositary:WorldEntryRepositary
):ViewModel() {

    val _countryList:MutableLiveData<ArrayList<Countries>> = MutableLiveData()
    val countryList:LiveData<ArrayList<Countries>>
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


    init {
        val countries = repositary.getCountries()
        if (countries != null){
            val tempList = arrayListOf<Countries>()
            tempList.addAll(countries)
            _countryList.value = tempList
        }
    }

    fun insertWorldEntry(country:String){
        val countryExists = repositary.countryExists(country)
        if (countryExists == 0) {
            val addEntry = AddWorldEntries(
                    country,
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
        if (!entriesList.isNullOrEmpty()){
            _worldEntriesList.value = convertArrayList
        }
    }

    fun deleteWorldEntries(countryName:String){
        repositary.deleteCountry(countryName)
    }

    fun getVaccineAndTestReportList(){
        val vaccineList = repositary.getVaccineList()
        _vaccineList.value = vaccineList
        val testReportList = repositary.getTestReportList()
        _testReportList.value = testReportList
    }

}