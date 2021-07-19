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
import com.cmg.vaccine.model.request.*
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
    var isIdnoExists = ObservableBoolean()

    var isFirstTimeSelectPlaceBirth:Boolean?=null

    var _identifierTypeList:MutableLiveData<List<IdentifierType>> = MutableLiveData()
    val identifierTypeList:LiveData<List<IdentifierType>>
        get() = _identifierTypeList

    var patientIdNoCharLength = ObservableInt()

    var _identifierTypeListForMYS:MutableLiveData<ArrayList<IdentifierType>> = MutableLiveData()
    val identifierTypeListForMYS:LiveData<ArrayList<IdentifierType>>
        get() = _identifierTypeListForMYS

    private var _identifierTypeListForOthers:MutableLiveData<ArrayList<IdentifierType>> = MutableLiveData()
    val identifierTypeListForOthers:LiveData<ArrayList<IdentifierType>>
        get() = _identifierTypeListForOthers

    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedItemIdTYpe.set(position)

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
        var identifierTypeForMYS = ArrayList<IdentifierType>()
        var identifierTypeForOthers = ArrayList<IdentifierType>()
        val getAllIdentifierType = repositary.getAllIdentifierType()
        if (!getAllIdentifierType.isNullOrEmpty()){
            _identifierTypeList.value = getAllIdentifierType

            getAllIdentifierType.forEach {
                when(it.identifierCode?.trim()){
                    "NNMYS" ->identifierTypeForMYS.add(it)
                    else ->identifierTypeForOthers.add(it)
                }
            }
            _identifierTypeListForMYS.value = identifierTypeForMYS
            _identifierTypeListForOthers.value = identifierTypeForOthers
        }



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
        listener?.onStarted()
        if ((!idNo.value.isNullOrEmpty()) and (nationalityCountryCode.value.equals("MYS"))){
            if (idNo.value?.length != patientIdNoCharLength.get()){
                listener?.onFailure("2Your ID Number is invalid")
                return
            }
        }


        if (isChecked.get()) {
                if (!fullName.value.isNullOrEmpty() and !email.value.isNullOrEmpty() and !contactNumber.value.isNullOrEmpty() and !dob.value.isNullOrEmpty()) {
                    if (email.value.equals(reTypeEmail.value)) {
                        if (isValidEmail(email.value!!)) {
                            if (validateDateFormat(dob.value!!)) {
                                if (validateTime(dobTime.value!!)) {



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

                                    if (!passportNumber.value.isNullOrEmpty()) {
                                        if ((repositary.checkPassportForDependent(passportNumber.value!!) > 0) or (repositary.checkPassportForPrinciple(passportNumber.value!!) > 0)){
                                            listener?.onShowToast("Passport Number Already Exsits")
                                            return
                                        }
                                    }

                                    if (!idNo.value.isNullOrEmpty()) {
                                        if ((repositary.checkIdnoForDependent(idNo.value!!) > 0) or (repositary.checkIdNoForPrinciple(idNo.value!!) > 0)){
                                            listener?.onShowToast("ID Number Already Exists")
                                            return
                                        }
                                    }

                                    val relationShips =
                                        view.context.resources.getStringArray(R.array.relationships)
                                    val relationShip = relationShips.get(relationshipItemPos.get())

                                    if (dobTime.value.isNullOrEmpty()) {
                                        dobTime.value = "00:00"
                                    }

                                    if (nationalityCountryCode.value.equals("MYS", false)) {
                                        idType.value =
                                            identifierTypeListForMYS.value?.get(selectedItemIdTYpe.get())?.identifierCode
                                    } else {
                                        idType.value =
                                            identifierTypeListForOthers.value?.get(
                                                selectedItemIdTYpe.get()
                                            )?.identifierCode
                                    }

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
                                    dependentRegReqData.nationalityCountry =nationalityCountryCode.value
                                        /*World.getCountryFrom(nationalityCountryCode.value).alpha3*/
                                    dependentRegReqData.gender = genderEnum.name
                                    dependentRegReqData.dob =
                                        dob.value + " " + dobTime.value + ":00"
                                    dependentRegReqData.placeOfBirth = birthPlaceCountryCode.value
                                        /*World.getCountryFrom(birthPlaceCountryCode.value).alpha3*/

                                    if (!idNo.value.isNullOrEmpty()) {
                                        dependentRegReqData.idType = idType.value
                                    }

                                    dependentRegReqData.passportNo = passportNumber.value?.trim()
                                    dependentRegReqData.passportExpiryDate =
                                        passportExpDate.value?.trim()
                                    dependentRegReqData.idNo = idNo.value?.trim()
                                    dependentRegReqData.masterSubsId = repositary.getParentSubId()

                                    dependentRegReq.data = dependentRegReqData

                                    Couritnes.main {
                                        try {
                                            val response =
                                                repositary.dependentSignUp(dependentRegReq)
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
                                                        nationalityCountryCode.value,
                                                    /*World.getCountryFrom(nationalityCountryCode.value).alpha3,*/
                                                    passportNumber.value?.trim(),
                                                    passportExpDate.value,
                                                        birthPlaceCountryCode.value,
                                                    /*World.getCountryFrom(birthPlaceCountryCode.value).alpha3,*/
                                                    relationShip
                                                )
                                                repositary.insertDependentSignUp(dependent)
                                                listener?.onSuccess(response.Message)
                                            } else {
                                                listener?.onFailure("2"+response.Message)
                                            }
                                        } catch (e: APIException) {
                                            listener?.onFailure("2"+e.message!!)
                                        } catch (e: NoInternetException) {
                                            listener?.onFailure("3"+e.message!!)
                                        } catch (e: SocketTimeoutException) {
                                            listener?.onFailure(e.message!!)
                                        }
                                    }
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
                        listener?.onShowToast("Email and Retype Email Mismatch")
                    }

                } else {
                    listener?.onShowToast("Please fill all mandatory fields")
                }

        }else{
            listener?.onShowToast("Please accept Terms and conditions")
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

            if (dependent?.idNo.isNullOrEmpty()){
                isIdnoExists.set(true)
            }else{
                idNo.value = dependent?.idNo
                isIdnoExists.set(false)
            }
            if (!dependent?.idType.isNullOrEmpty()) {
                idType.value = dependent?.idType
            }
            dob.value = dependent?.dob?.replace("/","")
            dobTime.value = dependent?.dobTime?.replace(":","")
            country.value = dependent?.nationalityCountry
            relationshipItemPos.set(selectedRelationShipPosition(dependent?.relationship!!,relationShipList!!))


            if (!dependent?.countryCode.isNullOrEmpty())
                countryCode.value = dependent?.countryCode?.toInt()

            nationalityCountryCode.value = dependent?.nationalityCountry
            birthPlaceCountryCode.value = dependent?.placeOfBirth
                    //nationalityCountryCode.value = World.getCountryFrom(dependent?.nationalityCountry).name
            //birthPlaceCountryCode.value = World.getCountryFrom(dependent?.placeOfBirth).name

            //nationalityCountryFlag.value = World.getCountryFrom(dependent?.nationalityCountry).flagResource
            //birthPlaceCountryFlag.value = World.getCountryFrom(dependent?.placeOfBirth).flagResource

            /*selectedItemNationalityCode.set(selectedCountryName(dependent?.nationalityCountry!!,countryList!!))
            selectedItemBirthPlaceCode.set(selectedCountryName(dependent?.placeOfBirth!!,countryList!!))*/
            if (!idType.value.isNullOrEmpty()) {
                selectedItemIdTYpe.set(selectedIdType(dependent?.idType!!, identifierTypeList.value!!))
            }

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

        if (!nationalityCountryCode.value.isNullOrEmpty()){
            if (nationalityCountryCode.value.equals("MYS",false)){
                patientIdNoCharLength.set(12)
            }else{
                patientIdNoCharLength.set(15)
            }
        }
    }

    private fun regExistingDependent(dependent: Dependent, view: View){

        val relationShips =
            view.context.resources.getStringArray(R.array.relationships)
        val relationShip = relationShips.get(relationshipItemPos.get())
        Couritnes.main {
            try {
                val existingDependentReq = ExistingDependentReq()
                val existingDependentReqData = ExistingDependentReqData()

                existingDependentReqData.masterSubsId = repositary.getParentSubId()
                existingDependentReqData.relationship = relationShip
                existingDependentReqData.subsId = dependent.subsId

                existingDependentReq.data = existingDependentReqData

                val response = repositary.existingDependent(existingDependentReq)
                if (response.StatusCode == 1){
                    dependent.relationship = relationShip
                    repositary.insertDependentSignUp(dependent)
                    listener?.onSuccess(response.Message)
                }else{
                    listener?.onFailure("2"+response.Message)
                }
            }catch (e:APIException){
                listener?.onFailure("2"+e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure("3"+e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onShowToast(e.message!!)
            }catch (e: Exception){
                listener?.onShowToast(e.message!!)
            }
        }
    }

    fun getDependentInfo(view: View){
        Log.d("enter","yes")
        Log.d("private_key",existingUserprivateKey.get()!!)
        var passportNo:String?=null
        var passportExpiryDate:String?=null
        var patientIdType:String?=null
        var patientIdNo:String?=null
        if (isChecked.get()) {
            val isDependentExists = repositary.getDependentUsingPrivateKey(existingUserprivateKey.get()!!)
            if (isDependentExists == null) {
                Couritnes.main {
                    try {
                        listener?.onStarted()
                        val response = repositary.getExistingUser(existingUserprivateKey.get()!!)

                        val responseBody = response.string()
                        val jsonBody = JSONObject(responseBody)
                        val jsonBodyFirst = jsonBody.getJSONObject("data")
                        val jsonBodySecond = jsonBodyFirst.getJSONObject("data")


                        if (jsonBodySecond.has("IDTypes")) {
                            val jsonIdTypeArray = jsonBodySecond.getJSONArray("IDTypes")
                            for (i in 0 until jsonIdTypeArray.length()) {
                                val item = jsonIdTypeArray.getJSONObject(i)
                                if (item.has("IdType")) {
                                    if (item.getString("IdType").equals("PPN", false)) {
                                        if (item.has("idNo")) {
                                            passportNo = item.getString("idNo")
                                        }
                                        if (item.has("passportExpiryDate")) {
                                            passportExpiryDate = item.getString("passportExpiryDate")
                                        }
                                    } else {
                                        patientIdType = item.getString("IdType")

                                        if (item.has("idNo")) {
                                            patientIdNo = item.getString("idNo")
                                        }
                                    }
                                }

                            }
                        }


                        if (jsonBodySecond.has("dob")) {
                            if (!jsonBodySecond.getString("dob").isNullOrEmpty()) {
                                val isoFormat = changeDateFormatBC(jsonBodySecond.getString("dob"))
                                var dobFormatArray = isoFormat?.split(" ")
                                dob.value = dobFormatArray?.get(0)
                                dobTime.value = dobFormatArray?.get(1)
                            }
                        }

                        val dependent = Dependent(
                                jsonBodySecond.getString("countryCode"),
                                dob.value,
                                dobTime.value,
                                jsonBodySecond.getString("email"),
                                jsonBodySecond.getString("fullName"),
                                jsonBodySecond.getString("gender"),
                                patientIdNo,
                                patientIdType,
                                repositary.getParentSubId(),
                                jsonBodySecond.getString("subsId"),
                                existingUserprivateKey.get()!!,
                                "",
                                jsonBodySecond.getString("mobileNumber"),
                                jsonBodySecond.getString("nationalityCountry"),
                                passportNo,
                                passportExpiryDate,
                                jsonBodySecond.getString("placeOfBirth"),
                                "",
                        )

                        if (!jsonBodySecond.getString("subsId").isNullOrEmpty()) {
                            val getDependent =
                                    repositary.getDependent(jsonBodySecond.getString("subsId"))
                            if (getDependent == null) {
                                regExistingDependent(dependent, view)
                            } else {
                                listener?.onFailure("2Sorry, this Dependent already added")
                            }
                        }

                        Log.d("response_body", responseBody)

                    } catch (e: APIException) {
                        listener?.onFailure("2" + e.message!!)
                    } catch (e: NoInternetException) {
                        listener?.onFailure("3" + e.message!!)
                    } catch (e: SocketTimeoutException) {
                        listener?.onShowToast(e.message!!)
                    } catch (e: JSONException) {
                        listener?.onShowToast("invalid")
                    } catch (e: Exception) {
                        listener?.onShowToast("2" + e.message!!)
                    }
                }
            }else{
                listener?.onFailure("2Dependent Already Exists!")
            }
        }else{
            listener?.onShowToast("Please accept Terms and conditions")
        }
    }

    fun updateProfile(view: View){
        if ((!idNo.value.isNullOrEmpty()) and (nationalityCountryCode.value.equals("MYS"))){
            if (idNo.value?.length != patientIdNoCharLength.get()){
                listener?.onFailure("Your ID Number is invalid")
                return
            }
        }

        /*if (!passportNumber.value.isNullOrEmpty()) {
            if ((repositary.checkPassportForDependent(passportNumber.value!!) > 0) or (repositary.checkPassportForPrinciple(passportNumber.value!!) > 0)){
                listener?.onShowToast("Passport Number Already Exsits")
                return
            }
        }

        if (!idNo.value.isNullOrEmpty()) {
            if ((repositary.checkIdnoForDependent(idNo.value!!) > 0) or (repositary.checkIdNoForPrinciple(idNo.value!!) > 0)){
                listener?.onShowToast("ID Number Already Exists")
                return
            }
        }*/

        isAllow = !(!currentEmail.equals(email.value) and !currentMobile.equals(contactNumber.value))
        val relationShips =
                view.context.resources.getStringArray(R.array.relationships)
        val relationShip =
                relationShips.get(relationshipItemPos.get())

        if (nationalityCountryCode.value.equals("MYS",false)) {
            idType.value =
                identifierTypeListForMYS.value?.get(selectedItemIdTYpe.get())?.identifierCode
        }else{
            idType.value =
                identifierTypeListForOthers.value?.get(selectedItemIdTYpe.get())?.identifierCode
        }

        if (isChecked.get()){

                if (!fullName.value.isNullOrEmpty() and !email.value.isNullOrEmpty() and !contactNumber.value.isNullOrEmpty()) {
                    if (isAllow) {
                        if (email.value.equals(reTypeEmail.value)) {
                            if (isValidEmail(email.value!!)) {
                                if (validateDateFormat(dob.value!!)) {
                                    if (validateTime(dobTime.value!!)) {
                                        listener?.onStarted()

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
                                        try {
                                            val updateProfileReq = UpdateProfileReq()
                                            val updateProfileReqData = UpdateProfileReqData()

                                            updateProfileReqData.firstName = fullName.value?.trim()
                                            updateProfileReqData.nationalityCountry =nationalityCountryCode.value
                                                /*World.getCountryFrom(nationalityCountryCode.value).alpha3*/
                                            updateProfileReqData.dob =
                                                dob.value + " " + dobTime.value + ":00"
                                            updateProfileReqData.placeOfBirth =birthPlaceCountryCode.value
                                                /*World.getCountryFrom(birthPlaceCountryCode.value).alpha3*/
                                            updateProfileReqData.countryCode =
                                                selectedItemContactCode.get()
                                            updateProfileReqData.passportNo =
                                                passportNumber.value?.trim()
                                            updateProfileReqData.passportExpiryDate =
                                                passportExpDate.value
                                            updateProfileReqData.gender = genderEnum.name
                                            updateProfileReqData.idNo = idNo.value?.trim()
                                            updateProfileReqData.subsId = dependent?.subsId
                                            updateProfileReqData.masterSubsId =
                                                dependent?.masterSubsId
                                            updateProfileReqData.relationship = relationShip
                                            updateProfileReqData.email = email.value?.trim()
                                            updateProfileReqData.mobileNumber =
                                                contactNumber.value?.trim()

                                            if (!idNo.value.isNullOrEmpty()) {
                                                updateProfileReqData.idType = idType.value
                                            }
                                            updateProfileReqData.privateKey = dependent?.privateKey

                                            updateProfileReq.data = updateProfileReqData
                                            repositary.saveEditProfileReq(updateProfileReq)
                                            listener?.onSuccess("")

                                        } catch (e: Exception) {
                                            listener?.onShowToast(e.message!!)
                                        }
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
                            listener?.onShowToast("Email and Retype Email Mismatch")
                        }
                    } else {
                        listener?.onShowToast("Sorry! You are not allowed to change Email Address and Mobile Number at same time")
                    }
                } else {
                    listener?.onShowToast("Please fill all Mandatory fields")
                }

        }else{
            listener?.onShowToast("Please accept Terms and conditions")
        }
    }
}