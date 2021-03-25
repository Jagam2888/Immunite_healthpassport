package com.cmg.vaccine.viewmodel

import android.view.View
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

    init {
        //val countries = repositary.getAllCountriesDB()
        val countries = World.getAllCountries()
        if (!countries.isNullOrEmpty()){
            _countries.value = countries
        }

        val getAllIdentifierType = repositary.getAllIdentifierType()
        if (getAllIdentifierType.isNullOrEmpty()){
            getIdentifierType()
        }else{
            _identifierTypeList.value = getAllIdentifierType
        }

    }

    private fun getIdentifierType(){
        Couritnes.main {
            try {
                val response = repositary.getIdentifierTypeFromAPI()
                if (!response.data.isNullOrEmpty()){
                    response.data.forEach {
                        val identifierType = IdentifierType(
                            it.identifierCode,
                            it.identifierDisplay,
                            it.identifierSeqno,
                            it.identifierStatus
                        )
                        repositary.insertIdentifierType(identifierType)
                    }
                    val getAllIdentifierType = repositary.getAllIdentifierType()
                    _identifierTypeList.value = getAllIdentifierType
                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    fun getPlaceOfBirthPos(){
        var alreadyStored = repositary.getUserData()
        val gson = Gson()
        val type: Type = object : TypeToken<User>() {}.type
        var userData = gson.fromJson<User>(alreadyStored, type)
        val pos = selectedCountryName(userData.placeBirth,countries.value!!)
        selectedItemNationalityCode.set(pos)
    }

    fun onRegister(view:View){
        listener?.onStarted()
        //if(!passportNo.value.isNullOrEmpty()) {
            if (isChecked.get()) {
                var alreadyStored = repositary.getUserData()
                val gson = Gson()
                val type: Type = object : TypeToken<User>() {}.type
                var userData = gson.fromJson<User>(alreadyStored, type)

                var nationality = ""
                if (!countries.value.isNullOrEmpty()){
                    nationality = countries.value?.get(selectedItemNationalityCode.get())?.alpha3!!
                }

                /*val idTypeList = view.context.resources.getStringArray(R.array.id_type)
                idType.value = idTypeList[selectedItemIdTYpe.get()]*/
                idType.value = identifierTypeList.value?.get(selectedItemIdTYpe.get())?.identifierCode

                userData.passportNumber = passportNo.value?.trim()
                userData.passportExpiryDate = passportExpDate.value
                userData.patientIdNo = idNo.value?.trim()
                userData.patientIdType = idType.value!!
                userData.nationality = nationality

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
                signUpReqData.idType = userData.patientIdType
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
            } else {
                listener?.onFailure("Please Read Terms and Condtition")
            }
        /*}else{
            listener?.onFailure("Passport Number Mandatory")
        }*/
    }
}