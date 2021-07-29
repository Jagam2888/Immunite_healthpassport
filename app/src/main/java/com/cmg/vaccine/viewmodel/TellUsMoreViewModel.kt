package com.cmg.vaccine.viewmodel

import android.view.View
import android.widget.AdapterView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.R
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.IdentifierType
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.request.SignUpReqData
import com.cmg.vaccine.repositary.TellUsRepositary
import com.cmg.vaccine.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.paperdb.Paper
import java.lang.Exception
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class TellUsMoreViewModel(
    private val repositary: TellUsRepositary
):ViewModel() {

    var address:MutableLiveData<String> = MutableLiveData()
    var city:MutableLiveData<String> = MutableLiveData()
    var state:MutableLiveData<String> = MutableLiveData()
    var passportNo:MutableLiveData<String> = MutableLiveData()
    var passportExpDate:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()
    var idType:MutableLiveData<String> = MutableLiveData()
    var selectedItemNationalityCode = ObservableInt()

    var isTermsRead:MutableLiveData<Boolean> = MutableLiveData()
    val selectedItemIdTYpe = ObservableInt()
    var isChecked = ObservableBoolean()

    var listener:SimpleListener?=null

    var _countries:MutableLiveData<List<Country>> = MutableLiveData()

    val countries:LiveData<List<Country>>
        get() = _countries

    val token = Paper.book().read(Passparams.FCM_TOKEN,"")

    var userSubId:MutableLiveData<String> = MutableLiveData()

    var _identifierTypeList:MutableLiveData<List<IdentifierType>> = MutableLiveData()
    val identifierTypeList:LiveData<List<IdentifierType>>
    get() = _identifierTypeList

    var _identifierTypeListForMYS:MutableLiveData<ArrayList<IdentifierType>> = MutableLiveData()
    val identifierTypeListForMYS:LiveData<ArrayList<IdentifierType>>
        get() = _identifierTypeListForMYS

    var _identifierTypeListForOthers:MutableLiveData<ArrayList<IdentifierType>> = MutableLiveData()
    val identifierTypeListForOthers:LiveData<ArrayList<IdentifierType>>
        get() = _identifierTypeListForOthers

    var nationalityCountryCode:MutableLiveData<String> = MutableLiveData()
    var nationalityCountryFlag:MutableLiveData<Int> = MutableLiveData()

    var patientIdNoCharLength = ObservableInt()




    val clicksListenerIdType = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedItemIdTYpe.set(position)

            /*if (!identifierTypeList.value?.isNullOrEmpty()!!){
                if (identifierTypeList.value!![position].identifierCode.equals("NNMYS")){
                    idNo.value = ""
                    patientIdNoCharLength.set(12)
                }
            }*/
        }
    }

    init {
        //val countries = repositary.getAllCountriesDB()
        var identifierTypeForMYS = ArrayList<IdentifierType>()
        var identifierTypeForOthers = ArrayList<IdentifierType>()
        val countries = World.getAllCountries()
        if (!countries.isNullOrEmpty()){
            _countries.value = countries
        }

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
    fun getPlaceOfBirthPos(){
        var alreadyStored = repositary.getUserData()
        val gson = Gson()
        val type: Type = object : TypeToken<User>() {}.type
        var userData = gson.fromJson<User>(alreadyStored, type)
        //nationalityCountryCode.value = World.getCountryFrom(userData.placeBirth).name
        nationalityCountryCode.value = userData.placeBirth
        nationalityCountryFlag.value = World.getFlagOf(userData.placeBirth)
        /*val pos = selectedCountryName(userData.placeBirth,countries.value!!)
        selectedItemNationalityCode.set(pos)*/

        if (!nationalityCountryCode.value.isNullOrEmpty()){
            if (nationalityCountryCode.value.equals("MYS",false)){
                patientIdNoCharLength.set(12)
            }else{
                patientIdNoCharLength.set(15)
            }
        }
    }

    fun onRegister(view:View){
        listener?.onStarted()
        if (!idNo.value.isNullOrEmpty()){
            if (idNo.value?.length != patientIdNoCharLength.get()){
                listener?.onShowToast("Your ID Number is invalid")
                return
            }
        }
            if (isChecked.get()) {

                if (nationalityCountryCode.value.equals("MYS")){
                    if (idNo.value.isNullOrEmpty()){
                        listener?.onShowToast("Malaysian should be enter Your Id number")
                        return
                    }
                }

                if (!nationalityCountryCode.value.equals("MYS")){
                    if((passportNo.value.isNullOrEmpty()) and (idNo.value.isNullOrEmpty())) {
                        listener?.onShowToast("Passport Number or Id number either one Mandatory")
                        return
                    }
                }

                if (!passportNo.value.isNullOrEmpty()){
                    if (passportExpDate.value.isNullOrEmpty()){
                        listener?.onShowToast("Please Enter Your Passport Expiry Date")
                        return
                    }
                }

                if (!passportExpDate.value.isNullOrEmpty()) {
                    if (passportNo.value.isNullOrEmpty()) {
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



                var alreadyStored = repositary.getUserData()
                val gson = Gson()
                val type: Type = object : TypeToken<User>() {}.type
                var userData = gson.fromJson<User>(alreadyStored, type)

                /*var nationality = ""
                if (!countries.value.isNullOrEmpty()){
                    nationality = countries.value?.get(selectedItemNationalityCode.get())?.alpha3!!
                }*/

                /*val idTypeList = view.context.resources.getStringArray(R.array.id_type)
                idType.value = idTypeList[selectedItemIdTYpe.get()]*/
                if (nationalityCountryCode.value.equals("MYS",false)) {
                    idType.value =
                        identifierTypeListForMYS.value?.get(selectedItemIdTYpe.get())?.identifierCode
                }else{
                    idType.value =
                        identifierTypeListForOthers.value?.get(selectedItemIdTYpe.get())?.identifierCode
                }
                //idType.value = identifierTypeList.value?.get(selectedItemIdTYpe.get())?.identifierCode

                userData.passportNumber = passportNo.value?.trim()
                userData.passportExpiryDate = passportExpDate.value
                userData.patientIdNo = idNo.value?.trim()
                if (!idNo.value.isNullOrEmpty()) {
                    userData.patientIdType = idType.value!!
                }
                userData.nationality = nationalityCountryCode.value!!

                val signUpReq = SignUpReq()
                var signUpReqData = SignUpReqData()

                signUpReqData.email = userData.email
                signUpReqData.firstName = userData.fullName
                signUpReqData.mobileNumber = userData.mobileNumber
                signUpReqData.gender = userData.gender
                signUpReqData.dob = userData.dob+" "+userData.dobTime+":00"
                signUpReqData.countryCode = userData.countryCode
                signUpReqData.placeOfBirth = userData.placeBirth
                signUpReqData.passportNo = userData.passportNumber
                signUpReqData.passportExpiryDate = userData.passportExpiryDate
                signUpReqData.idNo = userData.patientIdNo
                if (!idNo.value.isNullOrEmpty()) {
                    signUpReqData.idType = userData.patientIdType
                }
                signUpReqData.nationalityCountry = userData.nationality
                signUpReqData.token = token
                signUpReqData.mobileUniqueId = view.context.getDeviceUUID()

                signUpReq.data = signUpReqData
                Couritnes.main {
                    try {
                        val response = repositary.signUp(signUpReq)
                        if (response.StatusCode == 1 && !response.ParentSubscriberId.isNullOrEmpty()) {
                            repositary.saveUserEmail(userData.email)
                            repositary.saveUserSubId(response.ParentSubscriberId.trim())
                            userData.parentSubscriberId = response.ParentSubscriberId
                            userSubId.value = response.ParentSubscriberId
                            repositary.insertUser(userData)
                            listener?.onSuccess(response.Message)
                        }else{
                            listener?.onFailure("2"+response.Message)
                        }
                    } catch (e: APIException) {
                        listener?.onFailure("2"+e.message!!)
                    } catch (e: NoInternetException) {
                        listener?.onFailure("3"+e.message!!)
                    } catch (e: SocketTimeoutException) {
                        listener?.onShowToast(e.message!!)
                    }
                }
            } else {
                listener?.onShowToast("Please Read Terms and Condtition")
            }

    }
}