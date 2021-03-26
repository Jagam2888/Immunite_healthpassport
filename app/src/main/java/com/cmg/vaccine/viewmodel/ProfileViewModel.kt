package com.cmg.vaccine.viewmodel

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.R
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.*
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.request.UpdateProfileReqData
import com.cmg.vaccine.repositary.ProfileRepositary
import com.cmg.vaccine.util.*
import java.lang.Exception
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
    var passportExpDate:MutableLiveData<String> = MutableLiveData()
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

    var _countries:MutableLiveData<List<Country>> = MutableLiveData()

    val countries:LiveData<List<Country>>
        get() = _countries

    var countryList:List<Country>?=null
    var selectedItemNationalityCode = ObservableInt()
    var selectedItemBirthPlaceCode = ObservableInt()
    var selectedItemContactCode = ObservableField<String>()

    var dobViewFormat:MutableLiveData<String> = MutableLiveData()
    var passportDateViewFormat:MutableLiveData<String> = MutableLiveData()
    var placeBirthViewFormat:MutableLiveData<String> = MutableLiveData()
    var nationalityViewFormat:MutableLiveData<String> = MutableLiveData()

    var isAllow:Boolean = true
    var isUserNotAlreadyTest = ObservableBoolean()

    var currentEmail:String?=null
    var currentMobile:String?=null

    var userSubId:MutableLiveData<String> = MutableLiveData()

    var profileImageUri = ObservableField<String>()
    var calculateProgressBar = ObservableInt()

    var _identifierTypeList:MutableLiveData<List<IdentifierType>> = MutableLiveData()
    val identifierTypeList:LiveData<List<IdentifierType>>
        get() = _identifierTypeList




    /*val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedItemNationalityCode.set(position)
        }
    }*/

    init {
        //val countries = World.
       // countryList = repositary.getAllCountriesDB()
        countryList = World.getAllCountries()
        _countries.value = countryList

        var user = repositary.getUserData()
        isUserNotAlreadyTest.set(true)
        if (user != null) {
            if (!user.privateKey.isNullOrEmpty()) {
                val testReportList = repositary.getTestReportList(user.privateKey!!)
                if (testReportList.isNotEmpty()){
                    //if ((user.fullName == firstName.value) or (user.dob == dob.value) or (user.patientIdNo == idNo.value)){
                        isUserNotAlreadyTest.set(false)
                    //}
                }
            }
        }

        val getAllIdentifierType = repositary.getAllIdentifierType()
        if (!getAllIdentifierType.isNullOrEmpty()){
            _identifierTypeList.value = getAllIdentifierType
        }

    }

    fun saveProfileImage(img:String){
        val getUserData = repositary.getUserData()
        if (!img.isNullOrEmpty()){
            getUserData.profileImage = img
            repositary.updateUser(getUserData)
        }
        //repositary.setProfileImage(img)
    }

    fun getProfileImage():String?{
        return repositary.getProfileImage()
    }

    fun getDependentProfileImage(subsId: String):String?{
        return repositary.getDependentProfileImage(subsId)
    }

    fun loadParentData(){
        val user = repositary.getUserData()

        if(user != null) {
            firstName.value = user.fullName
            contactNumber.value = user.mobileNumber
            dob.value = user.dob

            dobTime.value = user.dobTime?.replace(":","")
            country.value = user.nationality
            passportNumber.value = user.passportNumber
            passportExpDate.value = user.passportExpiryDate
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


            if (!passportExpDate.value.isNullOrEmpty())
                passportDateViewFormat.value = changeDateFormatForViewProfile(passportExpDate.value!!)


            selectedItemNationalityCode.set(selectedCountryName(user.nationality,countryList!!))
            selectedItemBirthPlaceCode.set(selectedCountryName(user.placeBirth,countryList!!))
            selectedItemIdTYpe.set(selectedIdType(user.patientIdType!!,identifierTypeList.value!!))

            if (!user.countryCode.isNullOrEmpty())
                countryCode.value = user.countryCode.toInt()

            if (!dob.value.isNullOrEmpty())
                dobViewFormat.value = changeDateFormatForViewProfile(dob.value!!)

            dob.value = user.dob?.replace("/","")

            if (!placeBirth.value.isNullOrEmpty()) {
                placeBirthViewFormat.value = World.getCountryFrom(placeBirth.value!!).name
                //placeBirthViewFormat.value = getCountryNameUsingCode(placeBirth.value!!,countryList!!)
            }

            if (!country.value.isNullOrEmpty()){
                nationalityViewFormat.value = World.getCountryFrom(country.value!!).name

            }
                //nationalityViewFormat.value = getCountryNameUsingCode(country.value!!,countryList!!)

            currentEmail = email1.value
            currentMobile = contactNumber.value
            userSubId.value = user.parentSubscriberId
        }

        calculateProgressBar.set(calculateProgressBar())
    }

    fun removeDependent(subId: String){
        repositary.removeDependent(subId)
    }


    fun loadDependentData(subId:String){
        val dependent = repositary.getDependent(subId)
        if (dependent != null){
            firstName.value = dependent.firstName
            contactNumber.value = dependent.mobileNumber
            dob.value = dependent.dob
            country.value = dependent.countryCode
            passportNumber.value = dependent.passportNo
            passportExpDate.value = dependent.passportExpiryDate
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

            if (!passportExpDate.value.isNullOrEmpty())
                passportDateViewFormat.value = changeDateFormatForViewProfile(passportExpDate.value!!)

            if (!dependent.countryCode.isNullOrEmpty())
                countryCode.value = dependent.countryCode!!.toInt()

            if (!dob.value.isNullOrEmpty())
                dobViewFormat.value = changeDateFormatForViewProfile(dob.value!!)

            if (!dependent?.placeOfBirth.isNullOrEmpty()) {
                placeBirthViewFormat.value = World.getCountryFrom(dependent?.placeOfBirth!!).name
            }
                //placeBirthViewFormat.value = getCountryNameUsingCode(dependent?.placeOfBirth!!,countryList!!)

            if (!dependent?.nationalityCountry.isNullOrEmpty()){
                nationalityViewFormat.value = World.getCountryFrom(dependent?.nationalityCountry!!).name
            }
                //nationalityViewFormat.value = getCountryNameUsingCode(dependent?.nationalityCountry!!,countryList!!)
        }
        calculateProgressBar.set(calculateProgressBar())
    }

    fun loadChildList(){
        val dependentList = repositary.getDependentList(repositary.getSubId()!!)
        if (dependentList != null){
            _dependentList.value = dependentList
            dependentListCount.set(dependentList.size)
        }
    }

    fun onClick(view:View){
        listener?.onStarted()
        var user = repositary.getUserData()

        var placeBirth = ""
        if (!countryList.isNullOrEmpty()) {
            placeBirth =
                    countryList?.get(selectedItemBirthPlaceCode.get())?.alpha3!!
        }

        var nationality = ""
        if (!countryList.isNullOrEmpty()) {
            nationality =
                    countryList?.get(selectedItemNationalityCode.get())?.alpha3!!
        }

        /*val idTypeList =
                view.context.resources.getStringArray(R.array.id_type)
        idType.value = idTypeList[selectedItemIdTYpe.get()]*/
        idType.value = identifierTypeList.value?.get(selectedItemIdTYpe.get())?.identifierCode

        //remove first char if zero
        if (!contactNumber.value.isNullOrEmpty()) {
            if (contactNumber.value!!.startsWith("0")) {
                contactNumber.value =
                        contactNumber.value!!.drop(1)
            }
        }
        isAllow = !(!currentEmail.equals(email1.value) and !currentMobile.equals(contactNumber.value))
            try {
                if (!firstName.value.isNullOrEmpty() and !contactNumber.value.isNullOrEmpty() and !email1.value.isNullOrEmpty()) {
                    if (isAllow) {
                        if (email1.value.equals(reTypeEmail.value)) {
                            if (isChecked.get()) {
                                if (isValidEmail(email1.value!!)) {
                                    if (validateDateFormat(dob.value!!)) {
                                        if (validateTime(dobTime.value!!)) {

                                            val updateProfileReq = UpdateProfileReq()
                                            val updateProfileReqData = UpdateProfileReqData()

                                            updateProfileReqData.firstName = firstName.value
                                            updateProfileReqData.nationalityCountry = nationality
                                            updateProfileReqData.dob =
                                                dob.value + " " + dobTime.value + ":00"
                                            updateProfileReqData.subsId = user.parentSubscriberId
                                            updateProfileReqData.passportNo = passportNumber.value
                                            updateProfileReqData.passportExpiryDate = passportExpDate.value
                                            updateProfileReqData.gender = genderEnum.name
                                            updateProfileReqData.idNo = idNo.value
                                            updateProfileReqData.idType = idType.value
                                            updateProfileReqData.placeOfBirth = placeBirth
                                            updateProfileReqData.countryCode =
                                                selectedItemContactCode.get()!!
                                            updateProfileReqData.email = email1.value
                                            updateProfileReqData.mobileNumber = contactNumber.value


                                            updateProfileReq.data = updateProfileReqData
                                            repositary.saveEditProfileReq(updateProfileReq)
                                            listener?.onSuccess("")
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
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
    }

    fun calculateProgressBar():Int
    {
        var filled=0

        //Log.e("firstname",firstName.value.toString())
        if(!firstName.value.isNullOrBlank())
            filled+=1
        if(!contactNumber.value.isNullOrBlank())
            filled+=1
        if(!gender.value.isNullOrBlank())
            filled+=1
        if(!email1.value.isNullOrBlank())
            filled+=1
        if(!nationalityViewFormat.value.isNullOrBlank())
            filled+=1
        if(!passportNumber.value.isNullOrBlank())
            filled+=1
        if(!passportDateViewFormat.value.isNullOrBlank())
            filled+=1
        if(!idNo.value.isNullOrBlank())
            filled+=1
        if(!placeBirthViewFormat.value.isNullOrBlank())
            filled+=1
        if(!country.value.isNullOrBlank())
            filled+=1

        Log.e("Value",firstName.value.toString())
        Log.e("Value",contactNumber.value.toString())
        Log.e("Value",gender.value.toString())
        Log.e("Value",email1.value.toString())
        Log.e("Value",nationalityViewFormat.value.toString())
        Log.e("Value",passportNumber.value.toString())
        Log.e("Value",passportExpDate.toString())
        Log.e("Value",idNo.value.toString())
        Log.e("Value",placeBirthViewFormat.value.toString())
        Log.e("Value",country.value.toString())

        Log.e("filled", (filled*100 / 10 ).toString())

        return (filled*100 / 10 )
    }
}