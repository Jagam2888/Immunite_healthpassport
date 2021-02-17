package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.repositary.WorldEntryRepositary

class WorldEntryViewModel(
    private val repositary:WorldEntryRepositary
):ViewModel() {

    val _countryList:MutableLiveData<ArrayList<Countries>> = MutableLiveData()
    val countryList:LiveData<ArrayList<Countries>>
    get() = _countryList


    init {
        val countries = repositary.getCountries()
        if (countries != null){
            val tempList = arrayListOf<Countries>()
            tempList.addAll(countries)
            _countryList.value = tempList
        }
    }
}