package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.model.response.HomeResponse
import com.cmg.vaccine.repositary.HomeRepositary

class HomeViewModel(
        private val repositary: HomeRepositary
):ViewModel() {

    val _list:MutableLiveData<List<HomeResponse>> = MutableLiveData()

    val list:LiveData<List<HomeResponse>>
    get() = _list

    fun setList(value:List<HomeResponse>){
        _list.value = value
    }

    val firstName:MutableLiveData<String> = MutableLiveData()
    val lastName:MutableLiveData<String> = MutableLiveData()
    val idNo:MutableLiveData<String> = MutableLiveData()
    val gender:MutableLiveData<String> = MutableLiveData()
    val country:MutableLiveData<String> = MutableLiveData()

    init {
        val userData = repositary.getUserData()

        firstName.value = userData.firstName
        lastName.value = userData.lastName
        gender.value = userData.gender
        idNo.value = userData.patientIdNo
        country.value = userData.countryCode
    }
}