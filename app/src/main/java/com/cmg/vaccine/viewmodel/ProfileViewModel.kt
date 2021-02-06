package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.ProfileRepositary

class ProfileViewModel(
    private val repositary:ProfileRepositary
):ViewModel() {

    var firstName:MutableLiveData<String> = MutableLiveData()
    var residentalAddress:MutableLiveData<String> = MutableLiveData()
    var fullName:MutableLiveData<String> = MutableLiveData()
    var email1:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var country:MutableLiveData<String> = MutableLiveData()
    var city:MutableLiveData<String> = MutableLiveData()
    var state:MutableLiveData<String> = MutableLiveData()
    var gender:MutableLiveData<String> = MutableLiveData()
    var privateKey:MutableLiveData<String> = MutableLiveData()
    var passportNumber:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()
    var isChecked = ObservableBoolean()
    var dependentListCount = ObservableInt()

    var genderEnum:Gender = Gender.FEMALE

    var listener:SimpleListener?=null

    var _dependentList:MutableLiveData<List<Dependent>> = MutableLiveData()

    val dependentList:LiveData<List<Dependent>>
    get() = _dependentList


    init{
        val user = repositary.getUserData(repositary.getUserEmail()!!,"Y")

        if(user != null) {
            firstName.value = user.fullName
            contactNumber.value = user.mobileNumber
            gender.value = user.gender
            dob.value = user.dob
            residentalAddress.value = user.address
            city.value = user.city
            state.value = user.state
            country.value = user.countryCode
            passportNumber.value = user.passportNumber
            idNo.value = user.patientIdNo
            email1.value = user.email
            privateKey.value = user.privateKey
            user.gender.run {
                genderEnum = when(this){
                    "MALE" -> Gender.MALE
                    "FEMALE" -> Gender.FEMALE
                    else -> Gender.Other
                }
            }

            /*fullName.value = user.firstName + user.lastName
            firstName.value = user.firstName
            lastName.value = user.lastName
            email1.value = user.email
            if (user.backupEmail != null) {
                email2.value = user.backupEmail
            }
            contactNumber.value = user.mobileNumber
            idType.value = user.patientIdType
            idNumber.value = user.patientIdNo
            country.value = user.countryCode
            gender.value = user.gender
            privateKey.value = user.privateKey

            */
        }
    }

    fun loadChildList(){
        val dependentList = repositary.getDependentList(privateKey.value!!)
        if (dependentList != null && dependentList.isNotEmpty()){
            _dependentList.value = dependentList
            dependentListCount.set(dependentList.size)
        }
    }

    fun onClick(){
        var user = repositary.getUserData(repositary.getUserEmail()!!,"Y")

        user.state = state.value
        user.city = city.value
        user.address = residentalAddress.value
        user.gender = gender.value!!
        user.fullName = firstName.value!!
        user.mobileNumber = contactNumber.value!!
        user.passportNumber = passportNumber.value!!
        user.patientIdNo = idNo.value
        user.dob = dob.value!!

        if (isChecked.get()) {
            repositary.saveUser(user)
            listener?.onSuccess("Success")
        }else{
            listener?.onFailure("Please accept terms and conditions")
        }
    }
}