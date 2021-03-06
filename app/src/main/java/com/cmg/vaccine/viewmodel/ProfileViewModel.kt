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

    var birthPlaceCountryCode:MutableLiveData<String> = MutableLiveData()
    var nationalityCountryCode:MutableLiveData<String> = MutableLiveData()

    var birthPlaceCountryFlag:MutableLiveData<Int> = MutableLiveData()
    var nationalityCountryFlag:MutableLiveData<Int> = MutableLiveData()

    var dobViewFormat:MutableLiveData<String> = MutableLiveData()
    var passportDateViewFormat:MutableLiveData<String> = MutableLiveData()
    var placeBirthViewFormat:MutableLiveData<String> = MutableLiveData()
    var nationalityViewFormat:MutableLiveData<String> = MutableLiveData()

    var isAllow:Boolean = true
    var isIdnoExists = ObservableBoolean()

    var currentEmail:String?=null
    var currentMobile:String?=null

    var userSubId:MutableLiveData<String> = MutableLiveData()

    var profileImageUri = ObservableField<String>()
    var calculateProgressBar = ObservableInt()

    var _identifierTypeList:MutableLiveData<List<IdentifierType>> = MutableLiveData()
    val identifierTypeList:LiveData<List<IdentifierType>>
        get() = _identifierTypeList

    var _identifierTypeListForMYS:MutableLiveData<ArrayList<IdentifierType>> = MutableLiveData()
    val identifierTypeListForMYS:LiveData<ArrayList<IdentifierType>>
        get() = _identifierTypeListForMYS

    var _identifierTypeListForOthers:MutableLiveData<ArrayList<IdentifierType>> = MutableLiveData()
    val identifierTypeListForOthers:LiveData<ArrayList<IdentifierType>>
        get() = _identifierTypeListForOthers

    var patientIdNoCharLength = ObservableInt()
    var allowDependentCount = ObservableInt()

    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedItemIdTYpe.set(position)

        }
    }

    init {
        //val countries = World.
       // countryList = repositary.getAllCountriesDB()
        countryList = World.getAllCountries()
        _countries.value = countryList

        var user = repositary.getUserData()
        /*isUserNotAlreadyTest.set(true)
        if (user != null) {
            if (!user.privateKey.isNullOrEmpty()) {
                val testReportList = repositary.getTestReportList(user.privateKey!!)
                if (testReportList.isNotEmpty()){
                        isUserNotAlreadyTest.set(false)
                }
            }
        }*/
        var identifierTypeForOthers = ArrayList<IdentifierType>()
        var identifierTypeForMYS = ArrayList<IdentifierType>()
        val getAllIdentifierType = repositary.getAllIdentifierType()
        if (!getAllIdentifierType.isNullOrEmpty()){
            _identifierTypeList.value = getAllIdentifierType

            getAllIdentifierType.forEach {
                when(it.identifierCode?.trim()){
                    "NNMYS" ->identifierTypeForMYS.add(it)
                    else ->identifierTypeForOthers.add(it)
                }
                /*if (it.identifierCode.trim() == "NNMYS"){
                    identifierTypeForMYS.add(it)
                }else{
                    identifierTypeForOthers.add(it)
                }*/
            }
            _identifierTypeListForMYS.value = identifierTypeForMYS
            _identifierTypeListForOthers.value = identifierTypeForOthers
        }

        //Log.d("dep_count",repositary.getNoOfDependentCount())
        if (!repositary.getNoOfDependentCount().isNullOrEmpty()){
            val count = repositary.getNoOfDependentCount().toInt()
            allowDependentCount.set(count)
        }else{
            allowDependentCount.set(0)
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
            if ((!user.passportNumber.isNullOrEmpty()) and (user.passportNumber != "null")) {
                passportNumber.value = user.passportNumber
            }
            if (!user.passportExpiryDate.isNullOrEmpty()) {
                passportExpDate.value = user.passportExpiryDate
            }

            if (user.patientIdNo.isNullOrEmpty()){
                isIdnoExists.set(true)
            }else{
                idNo.value = user.patientIdNo
                isIdnoExists.set(false)
            }
            if ((!user.patientIdType.isNullOrEmpty()) and (!identifierTypeList.value.isNullOrEmpty())) {
                idType.value = identifierTypeList.value?.get(selectedIdType(user.patientIdType!!, identifierTypeList.value!!))?.identifierDisplay
            }
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
            if ((!identifierTypeList.value.isNullOrEmpty()) and (!user.patientIdType.isNullOrEmpty())) {
                selectedItemIdTYpe.set(selectedIdType(user.patientIdType!!, identifierTypeList.value!!))
            }

            /*nationalityCountryCode.value = World.getCountryFrom(user?.nationality).name
            birthPlaceCountryCode.value = World.getCountryFrom(user?.placeBirth).name*/

            nationalityCountryCode.value = user?.nationality
            birthPlaceCountryCode.value = user?.placeBirth

            nationalityCountryFlag.value = World.getCountryFrom(user?.nationality).flagResource
            birthPlaceCountryFlag.value = World.getCountryFrom(user?.placeBirth).flagResource

            if (!user.countryCode.isNullOrEmpty())
                countryCode.value = user.countryCode.toInt()

            if (!dob.value.isNullOrEmpty())
                dobViewFormat.value = changeDateFormatForViewProfile(dob.value!!)

            dob.value = user.dob?.replace("/","")

            if (!placeBirth.value.isNullOrEmpty()) {
                //placeBirthViewFormat.value = World.getCountryFrom(placeBirth.value!!).name
                /*placeBirthViewFormat.value = com.jdev.countryutil.Country.getCountryByISO(
                    getTwoAlpha(placeBirth.value!!)).name*/
                placeBirthViewFormat.value = getCountryName(getTwoAlpha(placeBirth.value!!)!!)
            }

            if (!country.value.isNullOrEmpty()){
                //nationalityViewFormat.value = World.getCountryFrom(country.value!!).name
                nationalityViewFormat.value = getCountryName(getTwoAlpha(country.value!!)!!)

            }
                //nationalityViewFormat.value = getCountryNameUsingCode(country.value!!,countryList!!)

            currentEmail = email1.value
            currentMobile = contactNumber.value
            userSubId.value = user.parentSubscriberId


            if (!nationalityCountryCode.value.isNullOrEmpty()){
                if (nationalityCountryCode.value.equals("MYS",false)){
                    patientIdNoCharLength.set(12)
                }else{
                    patientIdNoCharLength.set(15)
                }
            }

        }

        calculateProgressBar.set(calculateProgressBar())
    }

    fun removeDependent(subId: String){
        listener?.onStarted()
        Log.d("masterSubId",repositary.getMasterSubId()!!)
        Log.d("depSubId",subId)
        Couritnes.main {
            try {
                val response = repositary.removeDependentFromAPI(repositary.getMasterSubId()!!,subId)
                if (response.StatusCode == 1){
                    repositary.removeDependent(subId)
                    listener?.onSuccess(response.Message)
                }else{
                    listener?.onFailure("2"+response.Message)
                }
            }catch (e: APIException) {
                listener?.onFailure("2"+e.message!!)
            } catch (e: NoInternetException) {
                listener?.onFailure("3"+e.message!!)
            } catch (e: SocketTimeoutException) {
                listener?.onShowToast(e.message!!)
            } catch (e: Exception) {
                listener?.onShowToast(e.message!!)
            }
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
            passportExpDate.value = dependent.passportExpiryDate
            idNo.value = dependent.idNo
            privateKey.value = dependent.privateKey
            if ((!dependent.idType.isNullOrEmpty()) and (!identifierTypeList.value.isNullOrEmpty())) {
                idType.value = identifierTypeList.value?.get(selectedIdType(dependent.idType!!, identifierTypeList.value!!))?.identifierDisplay
            }
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
                //placeBirthViewFormat.value = World.getCountryFrom(dependent?.placeOfBirth!!).name
                placeBirthViewFormat.value = getCountryName(getTwoAlpha(dependent?.placeOfBirth!!)!!)
            }
                //placeBirthViewFormat.value = getCountryNameUsingCode(dependent?.placeOfBirth!!,countryList!!)

            if (!dependent?.nationalityCountry.isNullOrEmpty()){
                //nationalityViewFormat.value = World.getCountryFrom(dependent?.nationalityCountry!!).name
                nationalityViewFormat.value = getCountryName(getTwoAlpha(dependent?.nationalityCountry!!)!!)
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
        if (!idNo.value.isNullOrEmpty()){
            if (idNo.value?.length != patientIdNoCharLength.get()){
                listener?.onFailure("2Your ID Number is invalid")
                return
            }
        }
        var user = repositary.getUserData()

        /*var placeBirth = ""
        if (!countryList.isNullOrEmpty()) {
            placeBirth =
                    countryList?.get(selectedItemBirthPlaceCode.get())?.alpha3!!
        }

        var nationality = ""
        if (!countryList.isNullOrEmpty()) {
            nationality =
                    countryList?.get(selectedItemNationalityCode.get())?.alpha3!!
        }*/

        /*val idTypeList =
                view.context.resources.getStringArray(R.array.id_type)
        idType.value = idTypeList[selectedItemIdTYpe.get()]*/
        if (nationalityCountryCode.value.equals("MYS",false)) {
            idType.value =
                identifierTypeListForMYS.value?.get(selectedItemIdTYpe.get())?.identifierCode
        }else{
            idType.value =
                identifierTypeListForOthers.value?.get(selectedItemIdTYpe.get())?.identifierCode
        }
        //idType.value = identifierTypeList.value?.get(selectedItemIdTYpe.get())?.identifierCode

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

                                if (nationalityCountryCode.value.equals("MYS")) {
                                    if (idNo.value.isNullOrEmpty()) {
                                        listener?.onShowToast("Malaysian should be enter Your Id number")
                                        return
                                    }
                                }

                                if (!nationalityCountryCode.value.equals("MYS")) {
                                    if ((passportNumber.value.isNullOrEmpty()) and (idNo.value.isNullOrEmpty())) {
                                        listener?.onShowToast("Passport Number or Id number either one Mandatory")
                                        return
                                    }
                                }

                                if (!passportNumber.value.isNullOrEmpty()) {
                                    if (passportExpDate.value.isNullOrEmpty()) {
                                        listener?.onShowToast("Please Enter Your Passport Expiry Date")
                                        return
                                    }
                                }

                                if (!passportExpDate.value.isNullOrEmpty()) {
                                    if (passportNumber.value.isNullOrEmpty()) {
                                        listener?.onShowToast("Please Enter Your Passport Number")
                                        return
                                    }
                                }

                                if (!passportExpDate.value.isNullOrEmpty()){
                                    if (!validateDateFormatForPassport(passportExpDate.value!!)) {
                                        listener?.onShowToast("Sorry! Your Passport Already Expired or Invalid")
                                        return
                                    }
                                }


                                if (isChecked.get()) {
                                    if (isValidEmail(email1.value!!)) {
                                        if (validateDateFormat(dob.value!!)) {
                                            if (validateTime(dobTime.value!!)) {

                                                val updateProfileReq = UpdateProfileReq()
                                                val updateProfileReqData = UpdateProfileReqData()

                                                updateProfileReqData.firstName = firstName.value
                                                updateProfileReqData.nationalityCountry =
                                                    nationalityCountryCode.value
                                                updateProfileReqData.dob =
                                                    dob.value + " " + dobTime.value + ":00"
                                                updateProfileReqData.subsId =
                                                    user.parentSubscriberId
                                                updateProfileReqData.passportNo =
                                                    passportNumber.value
                                                updateProfileReqData.passportExpiryDate =
                                                    passportExpDate.value
                                                updateProfileReqData.gender = genderEnum.name
                                                updateProfileReqData.idNo = idNo.value
                                                if (!idNo.value.isNullOrEmpty()) {
                                                    updateProfileReqData.idType = idType.value
                                                }
                                                updateProfileReqData.placeOfBirth =
                                                    birthPlaceCountryCode.value
                                                updateProfileReqData.countryCode =
                                                    selectedItemContactCode.get()!!
                                                updateProfileReqData.email = email1.value
                                                updateProfileReqData.mobileNumber =
                                                    contactNumber.value
                                                updateProfileReqData.privateKey = user.privateKey


                                                updateProfileReq.data = updateProfileReqData
                                                repositary.saveEditProfileReq(updateProfileReq)
                                                listener?.onSuccess("")
                                            } else {
                                                listener?.onShowToast("Sorry! Invalid Birth Time")
                                            }
                                        } else {
                                            listener?.onShowToast("Sorry! Invalid Date of Birth")
                                        }
                                    } else {
                                        listener?.onShowToast("InValid Email")
                                    }
                                } else {
                                    listener?.onShowToast("Please accept terms and conditions")
                                }
                            } else {
                                listener?.onShowToast("Email and Retype Email Mismatch")
                            }
                        } else {
                            listener?.onShowToast("Sorry! You are not allowed to change Email Address and Mobile Number at same time")
                        }
                    } else {
                        listener?.onShowToast("Please fill all Mandatory fields")
                    }

            }catch (e:Exception){
                listener?.onShowToast(e.message!!)
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