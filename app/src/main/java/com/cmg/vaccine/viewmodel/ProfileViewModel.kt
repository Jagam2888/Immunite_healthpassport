package com.cmg.vaccine.viewmodel

import android.view.View
import android.widget.AdapterView
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
    var reTypeEmail:MutableLiveData<String> = MutableLiveData()
    var placeBirth:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var dobTime:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var country:MutableLiveData<String> = MutableLiveData()
    var city:MutableLiveData<String> = MutableLiveData()
    var state:MutableLiveData<String> = MutableLiveData()
    var gender:MutableLiveData<String> = MutableLiveData()
    var genderIcon:MutableLiveData<Int> = MutableLiveData()
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

    var dobViewFormat:MutableLiveData<String> = MutableLiveData()
    var placeBirthViewFormat:MutableLiveData<String> = MutableLiveData()
    var nationalityViewFormat:MutableLiveData<String> = MutableLiveData()

    var isAllow:Boolean = true

    var currentEmail:String?=null
    var currentMobile:String?=null

    var userSubId:MutableLiveData<String> = MutableLiveData()


    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedItemNationalityCode.set(position)
        }
    }

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

            dobTime.value = user.dobTime?.replace(":","")
            country.value = user.nationality
            passportNumber.value = user.passportNumber
            idNo.value = user.patientIdNo
            idType.value = user.patientIdType
            email1.value = user.email
            reTypeEmail.value = user.email
            privateKey.value = user.privateKey
            placeBirth.value = user.placeBirth
            user.gender.run {
                genderEnum = when(this){
                    "M" -> Gender.M
                    "F" -> Gender.F
                    else -> Gender.O
                }
            }

            if (user.gender == "M"){
                gender.value = "Male"
                genderIcon.value = R.drawable.male_icon
            }else if (user.gender == "F"){
                gender.value = "Female"
                genderIcon.value = R.drawable.female
            }else{
                gender.value = "Other"
                genderIcon.value = 0
            }


            selectedItemNationalityCode.set(selectedCountryName(user.nationality,countryList!!))
            selectedItemBirthPlaceCode.set(selectedCountryName(user.placeBirth,countryList!!))

            if (!user.countryCode.isNullOrEmpty())
                countryCode.value = user.countryCode.toInt()

            if (!dob.value.isNullOrEmpty())
                dobViewFormat.value = changeDateFormatForViewProfile(dob.value!!)

            dob.value = user.dob?.replace("/","")

            if (!placeBirth.value.isNullOrEmpty())
                placeBirthViewFormat.value = getCountryNameUsingCode(placeBirth.value!!,countryList!!)

            if (!country.value.isNullOrEmpty())
                nationalityViewFormat.value = getCountryNameUsingCode(country.value!!,countryList!!)

            currentEmail = email1.value
            currentMobile = contactNumber.value
            userSubId.value = user.parentSubscriberId

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
            idType.value = dependent.idType
            email1.value = dependent.email

            if (dependent.gender == "M"){
                gender.value = "Male"
                genderIcon.value = R.drawable.male_icon
            }else if (dependent.gender == "F"){
                gender.value = "Female"
                genderIcon.value = R.drawable.female
            }else{
                gender.value = "Other"
                genderIcon.value = 0
            }

            if (!dependent.countryCode.isNullOrEmpty())
                countryCode.value = dependent.countryCode!!.toInt()

            if (!dob.value.isNullOrEmpty())
                dobViewFormat.value = changeDateFormatForViewProfile(dob.value!!)

            if (!dependent?.placeOfBirth.isNullOrEmpty())
                placeBirthViewFormat.value = getCountryNameUsingCode(dependent?.placeOfBirth!!,countryList!!)

            if (!dependent?.nationalityCountry.isNullOrEmpty())
                nationalityViewFormat.value = getCountryNameUsingCode(dependent?.nationalityCountry!!,countryList!!)
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
        isAllow = !(!currentEmail.equals(email1.value) and !currentMobile.equals(contactNumber.value))
        Couritnes.main {
            try {
                if (!firstName.value.isNullOrEmpty() and !contactNumber.value.isNullOrEmpty() and !email1.value.isNullOrEmpty()) {
                    if (isAllow) {
                        if (email1.value.equals(reTypeEmail.value)) {
                            if (isChecked.get()) {
                                if (isValidEmail(email1.value!!)) {
                                    if (validateDateFormat(dob.value!!)) {
                                        if (validateTime(dobTime.value!!)) {
                                            var placeBirth = ""
                                            if (!countryList.isNullOrEmpty()) {
                                                placeBirth =
                                                    countryList?.get(selectedItemBirthPlaceCode.get())?.countryCodeAlpha!!
                                            }

                                            var nationality = ""
                                            if (!countryList.isNullOrEmpty()) {
                                                nationality =
                                                    countryList?.get(selectedItemNationalityCode.get())?.countryCodeAlpha!!
                                            }

                                            val idTypeList =
                                                view.context.resources.getStringArray(R.array.id_type)
                                            idType.value = idTypeList[selectedItemIdTYpe.get()]

                                            //remove first char if zero
                                            if (!contactNumber.value.isNullOrEmpty()) {
                                                if (contactNumber.value!!.startsWith("0")) {
                                                    contactNumber.value =
                                                        contactNumber.value!!.drop(1)
                                                }
                                            }

                                            var user = repositary.getUserData("Y")

                                            val updateProfileReq = UpdateProfileReq()
                                            val updateProfileReqData = UpdateProfileReqData()

                                            updateProfileReqData.firstName = firstName.value
                                            updateProfileReqData.nationalityCountry = nationality
                                            updateProfileReqData.dob =
                                                dob.value + " " + dobTime.value + ":00"
                                            updateProfileReqData.subsId = user.parentSubscriberId
                                            updateProfileReqData.passportNo = passportNumber.value
                                            updateProfileReqData.gender = genderEnum.name
                                            updateProfileReqData.idNo = idNo.value
                                            updateProfileReqData.idType = idType.value
                                            updateProfileReqData.placeOfBirth = placeBirth
                                            updateProfileReqData.countryCode =
                                                selectedItemContactCode.get()!!
                                            updateProfileReqData.email = email1.value
                                            updateProfileReqData.mobileNumber = contactNumber.value


                                            updateProfileReq.data = updateProfileReqData

                                            val response =
                                                repositary.updateProfile(updateProfileReq)
                                            if (response.StatusCode == 1) {
                                                user.state = state.value
                                                user.city = city.value
                                                user.address = residentalAddress.value
                                                user.gender = genderEnum.name
                                                user.fullName = firstName.value!!.trim()
                                                user.mobileNumber = contactNumber.value!!.trim()
                                                user.passportNumber = passportNumber.value?.trim()
                                                user.patientIdNo = idNo.value?.trim()
                                                user.dob = dob.value
                                                user.email = email1.value!!.trim()
                                                user.dobTime = dobTime.value
                                                repositary.updateUser(user)
                                                //repositary.saveUser(user)
                                                listener?.onSuccess(response.Message)
                                            } else {
                                                listener?.onFailure(response.Message)
                                            }
                                        }else{
                                            listener?.onFailure("Sorry! Invalid Birth Time")
                                        }
                                    }else{
                                        listener?.onFailure("Sorry! Invalid Date of Birth")
                                    }
                                } else {
                                    listener?.onFailure("InValid Email")
                                }
                            } else {
                                listener?.onFailure("Please accept terms and conditions")
                            }
                        }else{
                            listener?.onFailure("Email and Retype Email Mismatch")
                        }
                    }else{
                        listener?.onFailure("Sorry! You are not allowed to change Email Address and Mobile Number at same time")
                    }
                }else{
                    listener?.onFailure("Please fill all Mandatory fields")
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