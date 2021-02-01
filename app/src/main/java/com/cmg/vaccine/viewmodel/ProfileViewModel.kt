package com.cmg.vaccine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.repositary.ProfileRepositary

class ProfileViewModel(
    private val repositary:ProfileRepositary
):ViewModel() {

    var firstName:MutableLiveData<String> = MutableLiveData()
    var lastName:MutableLiveData<String> = MutableLiveData()
    var fullName:MutableLiveData<String> = MutableLiveData()
    var email1:MutableLiveData<String> = MutableLiveData()
    var email2:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var country:MutableLiveData<String> = MutableLiveData()
    var idType:MutableLiveData<String> = MutableLiveData()
    var idNumber:MutableLiveData<String> = MutableLiveData()
    var gender:MutableLiveData<String> = MutableLiveData()
    var privateKey:MutableLiveData<String> = MutableLiveData()

    var genderEnum:Gender = Gender.FEMALE


    init{
        val user = repositary.getUserData(repositary.getUserEmail()!!)

        if(user != null) {
            fullName.value = user.firstName + user.lastName
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

            user.gender.run {
                genderEnum = when(this){
                    "MALE" -> Gender.MALE
                    "FEMALE" -> Gender.FEMALE
                    else -> Gender.Other
                }
            }
        }
    }
}