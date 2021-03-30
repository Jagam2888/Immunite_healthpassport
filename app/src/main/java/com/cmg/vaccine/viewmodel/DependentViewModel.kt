package com.cmg.vaccine.viewmodel

import android.content.Context
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
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.IdentifierType
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.DependentRegReqData
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.request.UpdateProfileReqData
import com.cmg.vaccine.repositary.DependentRepositary
import com.cmg.vaccine.util.*
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException
import kotlin.concurrent.fixedRateTimer

class DependentViewModel(
    private val repositary: DependentRepositary
):ViewModel() {


    var principalName:MutableLiveData<String> = MutableLiveData()
    var fullName:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var dobTime:MutableLiveData<String> = MutableLiveData()
    var address = ObservableField<String>()
    var city = ObservableField<String>()
    var state = ObservableField<String>()
    var passportNumber:MutableLiveData<String> = MutableLiveData()
    var passportExpDate:MutableLiveData<String> = MutableLiveData()
    var country:MutableLiveData<String> = MutableLiveData()
    var gender:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()
    var idType:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var email:MutableLiveData<String> = MutableLiveData()
    var reTypeEmail:MutableLiveData<String> = MutableLiveData()
    var masterPrivateKey:String?=null
    var dependentPrivateKey:String?=null

    var relationshipItemPos = ObservableInt()
    var countryItemPos = ObservableInt()
    var nationalityItemPos = ObservableInt()
    var isChecked = ObservableBoolean()
    var isCheckedPrincipleAddress = ObservableBoolean()


    var parentAddress:String?=null
    var parentcity:String?=null
    var parentState:String?=null
    var dependent:Dependent?=null

    var genderEnum: Gender = Gender.M
    var listener:SimpleListener?=null

    var _countries:MutableLiveData<List<Country>> = MutableLiveData()

    val countries:LiveData<List<Country>>
        get() = _countries

    var countryList:List<Country>?=null
    var selectedItemNationalityCode = ObservableInt()
    var selectedItemBirthPlaceCode = ObservableInt()
    var selectedItemContactCode = ObservableField<String>()
    var selectedItemIdTYpe = ObservableInt()
    var countryCode:MutableLiveData<Int> = MutableLiveData()

    var birthPlaceCountryCode:MutableLiveData<String> = MutableLiveData()
    var nationalityCountryCode:MutableLiveData<String> = MutableLiveData()

    var birthPlaceCountryFlag:MutableLiveData<Int> = MutableLiveData()
    var nationalityCountryFlag:MutableLiveData<Int> = MutableLiveData()

    var isAllow:Boolean = true

    var currentEmail:String?=null
    var currentMobile:String?=null

    var userSubId:MutableLiveData<String> = MutableLiveData()
    var profileImageUri = ObservableField<String>()

    var existingUserprivateKey = ObservableField<String>()
    var isUserNotAlreadyTest = ObservableBoolean()

    var isFirstTimeSelectPlaceBirth:Boolean?=null

    var _identifierTypeList:MutableLiveData<List<IdentifierType>> = MutableLiveData()
    val identifierTypeList:LiveData<List<IdentifierType>>
        get() = _identifierTypeList

    var patientIdNoCharLength = ObservableInt()

    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedItemIdTYpe.set(position)
            if (!identifierTypeList.value?.isNullOrEmpty()!!){
                if (identifierTypeList.value!![position].identifierCode.equals("NNMYS")){
                    idNo.value = ""
                    patientIdNoCharLength.set(12)
                }
            }
        }
    }

    fun saveProfileImage(imgUri:String,subsId: String){
        val getDependent = repositary.getDependent(subsId)
        if (getDependent != null){
            getDependent.profileImage = imgUri
            repositary.updateDependent(getDependent)
        }
    }

    fun getProfileImage(subsId: String):String?{
        return repositary.getProfileImage(subsId)
    }

    init {
        isUserNotAlreadyTest.set(true)
        isFirstTimeSelectPlaceBirth = true

        val parentUser = repositary.getUserData()
        if (parentUser != null) {
            masterPrivateKey = parentUser.privateKey
            email.value = parentUser.email
            reTypeEmail.value = parentUser.email
            contactNumber.value = parentUser.mobileNumber
            if (!parentUser?.countryCode.isNullOrEmpty())
                countryCode.value = parentUser?.countryCode?.toInt()
            principalName.value = parentUser.fullName
        }
        /*countryList = repositary.getAllCountriesDB()
        _countries.value = countryList*/
        countryList = World.getAllCountries()
        _countries.value = countryList

        dobTime.value = "1200"

        /*if (!parentUser.privateKey.isNullOrEmpty()) {
            val dependent = repositary.getDependent(parentUser.parentSubscriberId!!)
            if (dependent != null){
                if (!dependent.privateKey.isNullOrEmpty()) {
                    val testReportList = repositary.getTestReportList(dependent.privateKey!!)
                    if (testReportList.isNotEmpty()){
                        isUserNotAlreadyTest.set(false)
                    }
                }
            }
        }*/

        val getAllIdentifierType = repositary.getAllIdentifierType()
        if (!getAllIdentifierType.isNullOrEmpty()){
            _identifierTypeList.value = getAllIdentifierType
        }

        patientIdNoCharLength.set(15)

    }

    fun setCurrentCountry(country:String){
        //countryList = repositary.getAllCountriesDB()
        countryList = World.getAllCountries()
        if (!countryList.isNullOrEmpty()){
            val pos = getCurrentCountry(country,countryList!!)
            val pos1 = World.getCountryFrom(country)

            Log.d("country_pos",pos.toString())
            Log.d("country_pos1",pos1.toString())

            selectedItemBirthPlaceCode.set(pos)
            selectedItemNationalityCode.set(pos)

            //birthPlaceCountryCode.set(World.getCountryFrom(country).)
            //selectedItemNationalityCode.set(5)
        }
    }

    fun onClick(view:View) {
        if (isChecked.get()) {
            if (!fullName.value.isNullOrEmpty() and !email.value.isNullOrEmpty() and !contactNumber.value.isNullOrEmpty() and !dob.value.isNullOrEmpty()) {
                if (email.value.equals(reTypeEmail.value)) {
                    if (isValidEmail(email.value!!)) {
                        if (validateDateFormat(dob.value!!)) {
                            if (validateTime(dobTime.value!!)) {
                                listener?.onStarted()

                                val relationShips =
                                    view.context.resources.getStringArray(R.array.relationships)
                                val relationShip = relationShips.get(relationshipItemPos.get())

                                if (dobTime.value.isNullOrEmpty()) {
                                    dobTime.value = "00:00"
                                }

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
                                idType.value = identifierTypeList.value?.get(selectedItemIdTYpe.get())?.identifierCode

                                //remove first char if zero
                                if (!contactNumber.value.isNullOrEmpty()) {
                                    if (contactNumber.value!!.startsWith("0")) {
                                        contactNumber.value = contactNumber.value!!.drop(1)
                                    }
                                }


                                val dependentRegReq = DependentRegReq()

                                val dependentRegReqData = DependentRegReqData()
                                dependentRegReqData.firstName = fullName.value?.trim()
                                dependentRegReqData.countryCode = selectedItemContactCode.get()
                                dependentRegReqData.mobileNumber = contactNumber.value?.trim()
                                dependentRegReqData.email = email.value?.trim()
                                dependentRegReqData.relationship = relationShip
                                dependentRegReqData.nationalityCountry = World.getCountryFrom(nationalityCountryCode.value).alpha3
                                dependentRegReqData.gender = genderEnum.name
                                dependentRegReqData.dob = dob.value + " " + dobTime.value + ":00"
                                dependentRegReqData.placeOfBirth = World.getCountryFrom(birthPlaceCountryCode.value).alpha3
                                dependentRegReqData.idType = idType.value

                                dependentRegReqData.passportNo = passportNumber.value?.trim()
                                dependentRegReqData.passportExpiryDate = passportExpDate.value?.trim()
                                dependentRegReqData.idNo = idNo.value?.trim()
                                dependentRegReqData.masterSubsId = repositary.getParentSubId()

                                dependentRegReq.data = dependentRegReqData

                                Couritnes.main {
                                    try {
                                        val response = repositary.dependentSignUp(dependentRegReq)
                                        if (response.StatusCode == 1) {

                                            val dependent = Dependent(
                                                selectedItemContactCode.get(),
                                                dob.value,
                                                dobTime.value,
                                                email.value?.trim(),
                                                fullName.value?.trim(),
                                                genderEnum.name,
                                                idNo.value?.trim(),
                                                idType.value,
                                                repositary.getParentSubId(),
                                                response.SubsId,
                                                response.privateKey,
                                                    null,
                                                contactNumber.value?.trim(),
                                                World.getCountryFrom(nationalityCountryCode.value).alpha3,
                                                passportNumber.value?.trim(),
                                                passportExpDate.value,
                                                World.getCountryFrom(birthPlaceCountryCode.value).alpha3,
                                                relationShip
                                            )
                                            repositary.insertDependentSignUp(dependent)
                                            listener?.onSuccess(response.Message)
                                        } else {
                                            listener?.onFailure(response.Message)
                                        }
                                    } catch (e: APIException) {
                                        listener?.onFailure(e.message!!)
                                    } catch (e: NoInternetException) {
                                        listener?.onFailure(e.message!!)
                                    } catch (e: SocketTimeoutException) {
                                        listener?.onFailure(e.message!!)
                                    }
                                }
                            }else{
                                listener?.onFailure("Sorry! Invalid Birth Time")
                            }
                        }else{
                            listener?.onFailure("Sorry! Invalid Date of Birth")
                        }
                    }else{
                        listener?.onFailure("InValid Email")
                    }
                }else{
                    listener?.onFailure("Email and Retype Email Mismatch")
                }

            }else{
                listener?.onFailure("Please fill all mandatory fields")
            }
        }else{
            listener?.onFailure("Please accept Terms and conditions")
        }
    }

    fun loadProfileData(context: Context,subsId:String){
        dependentPrivateKey = subsId
        dependent = repositary.getDependent(subsId!!)
        val relationShipList = context.resources.getStringArray(R.array.relationships).toList()
        //val relationShip = relationShipList.get(relationshipItemPos.get())

        if (dependent != null){
            fullName.value = dependent?.firstName
            //address.set(dependent?.residentialAddress)
            email.value = dependent?.email
            reTypeEmail.value = dependent?.email
            contactNumber.value = dependent?.mobileNumber
            //city.set(dependent?.townCity)
            //state.set(dependent?.provinceState)
            passportNumber.value = dependent?.passportNo
            passportExpDate.value = dependent?.passportExpiryDate
            idNo.value = dependent?.idNo
            idType.value = dependent?.idType
            dob.value = dependent?.dob?.replace("/","")
            dobTime.value = dependent?.dobTime?.replace(":","")
            country.value = dependent?.nationalityCountry
            relationshipItemPos.set(selectedRelationShipPosition(dependent?.relationship!!,relationShipList!!))


            if (!dependent?.countryCode.isNullOrEmpty())
                countryCode.value = dependent?.countryCode?.toInt()

            nationalityCountryCode.value = World.getCountryFrom(dependent?.nationalityCountry).name
            birthPlaceCountryCode.value = World.getCountryFrom(dependent?.placeOfBirth).name

            nationalityCountryFlag.value = World.getCountryFrom(dependent?.nationalityCountry).flagResource
            birthPlaceCountryFlag.value = World.getCountryFrom(dependent?.placeOfBirth).flagResource

            /*selectedItemNationalityCode.set(selectedCountryName(dependent?.nationalityCountry!!,countryList!!))
            selectedItemBirthPlaceCode.set(selectedCountryName(dependent?.placeOfBirth!!,countryList!!))*/
            selectedItemIdTYpe.set(selectedIdType(dependent?.idType!!,identifierTypeList.value!!))

            dependent?.gender.run {
                genderEnum = when(this){
                    "M" -> Gender.M
                    "F" -> Gender.F
                    else -> Gender.O
                }
            }

            if (dependent?.gender == "M"){
                gender.value = "Male"
            }else if (dependent?.gender == "F"){
                gender.value = "Female"
            }else{
                gender.value = "Other"
            }
        }
        currentEmail = email.value
        currentMobile = contactNumber.value

        userSubId.value = dependent?.subsId
    }

    fun updateProfile(view: View){
        isAllow = !(!currentEmail.equals(email.value) and !currentMobile.equals(contactNumber.value))
        val relationShips =
                view.context.resources.getStringArray(R.array.relationships)
        val relationShip =
                relationShips.get(relationshipItemPos.get())

        /*var placeBirth = ""
        if (!countryList.isNullOrEmpty()) {
            placeBirth =
                    countryList?.get(selectedItemBirthPlaceCode.get())?.alpha3!!
            //placeBirth = World.getCountryFrom(selectedItemBirthPlaceCode.get()).alpha3
        }

        var nationality = ""
        if (!countryList.isNullOrEmpty()) {
            nationality =
                    countryList?.get(selectedItemNationalityCode.get())?.alpha3!!
        }*/

        /*val idTypeList =
                view.context.resources.getStringArray(R.array.id_type)
        idType.value = idTypeList[selectedItemIdTYpe.get()]*/
        idType.value = identifierTypeList.value?.get(selectedItemIdTYpe.get())?.identifierCode

        if (isChecked.get()){
            if (!fullName.value.isNullOrEmpty() and !email.value.isNullOrEmpty() and !contactNumber.value.isNullOrEmpty()) {
                if (isAllow) {
                    if (email.value.equals(reTypeEmail.value)) {
                        if (isValidEmail(email.value!!)) {
                            if (validateDateFormat(dob.value!!)) {
                                if (validateTime(dobTime.value!!)) {
                                    listener?.onStarted()
                                        try {
                                            val updateProfileReq = UpdateProfileReq()
                                            val updateProfileReqData = UpdateProfileReqData()

                                            updateProfileReqData.firstName = fullName.value?.trim()
                                            updateProfileReqData.nationalityCountry = World.getCountryFrom(nationalityCountryCode.value).alpha3
                                            updateProfileReqData.dob =
                                                dob.value + " " + dobTime.value + ":00"
                                            updateProfileReqData.placeOfBirth = World.getCountryFrom(birthPlaceCountryCode.value).alpha3
                                            updateProfileReqData.countryCode =
                                                selectedItemContactCode.get()
                                            updateProfileReqData.passportNo =
                                                passportNumber.value?.trim()
                                            updateProfileReqData.passportExpiryDate = passportExpDate.value
                                            updateProfileReqData.gender = genderEnum.name
                                            updateProfileReqData.idNo = idNo.value?.trim()
                                            updateProfileReqData.subsId = dependent?.subsId
                                            updateProfileReqData.masterSubsId =
                                                dependent?.masterSubsId
                                            updateProfileReqData.relationship = relationShip
                                            updateProfileReqData.email = email.value?.trim()
                                            updateProfileReqData.mobileNumber =
                                                contactNumber.value?.trim()
                                            updateProfileReqData.idType = idType.value

                                            updateProfileReq.data = updateProfileReqData
                                            repositary.saveEditProfileReq(updateProfileReq)
                                            listener?.onSuccess("")

                                        } catch (e: Exception) {
                                            listener?.onFailure(e.message!!)
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
                    }else{
                        listener?.onFailure("Email and Retype Email Mismatch")
                    }
                }else{
                    listener?.onFailure("Sorry! You are not allowed to change Email Address and Mobile Number at same time")
                }
            }else{
                listener?.onFailure("Please fill all Mandatory fields")
            }
        }else{
            listener?.onFailure("Please accept Terms and conditions")
        }
    }
}