package com.cmg.vaccine.viewmodel

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.R
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.request.UpdateProfileReqData
import com.cmg.vaccine.repositary.ProfileRepositary
import com.cmg.vaccine.util.*
import java.net.SocketTimeoutException

class ProfileViewModel(
    private val repositary:ProfileRepositary
):ViewModel() {

    var firstName:MutableLiveData<String> = MutableLiveData()
    var residentalAddress:MutableLiveData<String> = MutableLiveData()
    var fullName:MutableLiveData<String> = MutableLiveData()
    var email1:MutableLiveData<String> = MutableLiveData()
    var placeBirth:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var dobTime:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var country:MutableLiveData<String> = MutableLiveData()
    var city:MutableLiveData<String> = MutableLiveData()
    var state:MutableLiveData<String> = MutableLiveData()
    var gender:MutableLiveData<String> = MutableLiveData()
    var privateKey:MutableLiveData<String> = MutableLiveData()
    var passportNumber:MutableLiveData<String> = MutableLiveData()
    var countryCode:MutableLiveData<Int> = MutableLiveData()
    var user:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()
    var idType:MutableLiveData<String> = MutableLiveData()
    var isChecked = ObservableBoolean()
    var dependentListCount = ObservableInt()
    var selectedItemIdTYpe = ObservableInt()

    var genderEnum:Gender = Gender.F

    var listener:SimpleListener?=null

    var _dependentList:MutableLiveData<List<Dependent>> = MutableLiveData()

    val dependentList:LiveData<List<Dependent>>
    get() = _dependentList

    var _countries:MutableLiveData<List<Countries>> = MutableLiveData()

    val countries:LiveData<List<Countries>>
        get() = _countries

    var countryList:List<Countries>?=null
    var selectedItemNationalityCode = ObservableInt()
    var selectedItemBirthPlaceCode = ObservableInt()
    var selectedItemContactCode = ObservableField<String>()

    init {
        countryList = repositary.getAllCountriesDB()
        _countries.value = countryList
    }

    fun loadParentData(){
        val user = repositary.getUserData("Y")

        if(user != null) {
            firstName.value = user.fullName
            contactNumber.value = user.mobileNumber
            dob.value = user.dob
            residentalAddress.value = user.address
            city.value = user.city
            state.value = user.state
            country.value = user.countryCode
            passportNumber.value = user.passportNumber
            idNo.value = user.patientIdNo
            email1.value = user.email
            privateKey.value = user.privateKey
            placeBirth.value = user.placeBirth
            dobTime.value = user.dobTime
            user.gender.run {
                genderEnum = when(this){
                    "M" -> Gender.M
                    "F" -> Gender.F
                    else -> Gender.O
                }
            }

            if (user.gender == "M"){
                gender.value = "Male"
            }else if (user.gender == "F"){
                gender.value = "Female"
            }else{
                gender.value = "Other"
            }

            selectedItemNationalityCode.set(selectedCountryName(user.nationality,countryList!!))
            selectedItemBirthPlaceCode.set(selectedCountryName(user.placeBirth,countryList!!))

            if (!user.countryCode.isNullOrEmpty())
                countryCode.value = user.countryCode.toInt()

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



    fun setCurrentCountry(country:String){

        if (!countryList.isNullOrEmpty()){
            val pos = getCurrentCountry(country,countryList!!)
            selectedItemNationalityCode.set(pos)
            //selectedItemNationalityCode.set(5)
        }
    }

    fun loadDependentData(subId:String){
        val dependent = repositary.getDependent(subId)
        if (dependent != null){
            firstName.value = dependent.firstName
            contactNumber.value = dependent.mobileNumber
            dob.value = dependent.dob
            country.value = dependent.countryCode
            passportNumber.value = dependent.passportNo
            idNo.value = dependent.idNo
            email1.value = dependent.email

            if (dependent.gender == "M"){
                gender.value = "Male"
            }else if (dependent.gender == "F"){
                gender.value = "Female"
            }else{
                gender.value = "Other"
            }
        }
    }

    fun loadChildList(){
        val dependentList = repositary.getDependentList(repositary.getSubId()!!)
        if (dependentList != null && dependentList.isNotEmpty()){
            _dependentList.value = dependentList
            dependentListCount.set(dependentList.size)
        }
    }

    fun onClick(view:View){
        listener?.onStarted()
        Couritnes.main {
            try {
                if (isChecked.get()) {

                    var placeBirth = ""
                    if (!countryList.isNullOrEmpty()){
                        placeBirth = countryList?.get(selectedItemBirthPlaceCode.get())?.countryCodeAlpha!!
                    }

                    var nationality = ""
                    if (!countryList.isNullOrEmpty()){
                        nationality = countryList?.get(selectedItemNationalityCode.get())?.countryCodeAlpha!!
                    }

                    val idTypeList = view.context.resources.getStringArray(R.array.id_type)
                    idType.value = idTypeList[selectedItemIdTYpe.get()]

                    //remove first char if zero
                    if (!contactNumber.value.isNullOrEmpty()){
                        if (contactNumber.value!!.startsWith("0")){
                            contactNumber.value = contactNumber.value!!.drop(1)
                        }
                    }

                    var user = repositary.getUserData("Y")

                    val updateProfileReq = UpdateProfileReq()
                    val updateProfileReqData = UpdateProfileReqData()

                    updateProfileReqData.firstName = firstName.value
                    updateProfileReqData.nationalityCountry = nationality
                    updateProfileReqData.dob = dob.value+" "+dobTime.value
                    updateProfileReqData.subsId = user.parentSubscriberId
                    updateProfileReqData.passportNo = passportNumber.value
                    updateProfileReqData.gender = genderEnum.name
                    updateProfileReqData.idNo = idNo.value
                    updateProfileReqData.idType = idType.value
                    updateProfileReqData.placeOfBirth = placeBirth
                    updateProfileReqData.countryCode = selectedItemContactCode.get()!!
                    updateProfileReqData.email = email1.value
                    updateProfileReqData.mobileNumber = contactNumber.value
                    /*updateProfileReqData.residentialAddress = residentalAddress.value
                    updateProfileReqData.townCity = city.value
                    updateProfileReqData.provinceState = state.value*/

                    updateProfileReq.data = updateProfileReqData

                    val response = repositary.updateProfile(updateProfileReq)
                    if (response.StatusCode == 1){
                        user.state = state.value
                        user.city = city.value
                        user.address = residentalAddress.value
                        user.gender = gender.value!!
                        user.fullName = firstName.value!!
                        user.mobileNumber = contactNumber.value!!
                        user.passportNumber = passportNumber.value!!
                        user.patientIdNo = idNo.value!!
                        user.dob = dob.value!!
                        user.dobTime = dobTime.value!!
                        repositary.saveUser(user)
                        listener?.onSuccess(response.Message)
                    }else{
                        listener?.onFailure(response.Message)
                    }
                }else{
                    listener?.onFailure("Please accept terms and conditions")
                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }
        }
    }
}