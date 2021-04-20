package com.cmg.vaccine.viewmodel

import android.os.Build
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.IdentifierType
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.SignUpRepositary
import com.cmg.vaccine.util.*
import com.hbb20.CountryCodePicker
import java.lang.Exception
import java.net.SocketTimeoutException
import java.time.YearMonth
import java.util.*

class SignupViewModel(
        private val signUpRepositary: SignUpRepositary
):ViewModel() {

    var fullName:MutableLiveData<String> = MutableLiveData()
    var email:MutableLiveData<String> = MutableLiveData()
    var reTypeEmail:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var dobTime:MutableLiveData<String> = MutableLiveData()
    var gender: Gender = Gender.M
    var selectedItemContactCode = ObservableField<String>()
    var selectedItemNationalityCode = ObservableInt()

    var birthPlaceCountryCode:MutableLiveData<String> = MutableLiveData()

    var birthPlaceCountryFlag:MutableLiveData<Int> = MutableLiveData()

    var listener:SimpleListener?=null

    var _countries:MutableLiveData<List<Country>> = MutableLiveData()

    val countries:LiveData<List<Country>>
        get() = _countries

    var countryList:List<Country>?=null

    fun setCurrentCountry(country:String){
        //countryList = signUpRepositary.getAllCountriesDB()
        countryList = World.getAllCountries()
        if (!countryList.isNullOrEmpty()){
            val pos = getCurrentCountry(country,countryList!!)
            selectedItemNationalityCode.set(pos)
            //selectedItemNationalityCode.set(5)
        }
    }

    init {
        listener?.onStarted()
        //countryList = signUpRepositary.getAllCountriesDB()
        countryList = World.getAllCountries()
        _countries.value = countryList

        dobTime.value = "1200"


    }

    private fun getIdentifierType(){
        Couritnes.main {
            try {
                val response = signUpRepositary.getIdentifierTypeFromAPI()
                if (!response.data.isNullOrEmpty()){
                    signUpRepositary.deleteAllIdentifier()
                    response.data.forEach {
                        val identifierType = IdentifierType(
                            it.identifierCode,
                            it.identifierDisplay,
                            it.identifierSeqno,
                            it.identifierStatus
                        )
                        signUpRepositary.insertIdentifierType(identifierType)
                    }

                }
                listener?.onSuccess("success")
            }catch (e:APIException){
                listener?.onShowToast(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure("3"+e.message!!)
            }catch (e:Exception){
                listener?.onShowToast(e.message!!)
            }
        }
    }

    fun onSignUp(){
        listener?.onStarted()
        /*var placeBirth = ""
        if (!countryList.isNullOrEmpty()){
            placeBirth = countryList?.get(selectedItemNationalityCode.get())?.alpha3!!
        }*/
        if(!fullName.value.isNullOrEmpty()and !email.value.isNullOrEmpty() and !contactNumber.value.isNullOrEmpty()) {
            if (email.value.equals(reTypeEmail.value)) {
                if (dobTime.value.isNullOrEmpty()){
                    dobTime.value = "00:00"
                }

                //remove first char if zero
                if (!contactNumber.value.isNullOrEmpty()){
                    if (contactNumber.value!!.startsWith("0") and selectedItemContactCode.get().equals("60")){
                        contactNumber.value = contactNumber.value!!.drop(1)
                    }
                }

                if (isValidEmail(email.value!!)) {
                    if (validateDateFormat(dob.value!!)) {
                        if (validateTime(dobTime.value!!)) {
                            var user = User(
                                fullName.value!!.trim(),
                                email.value!!.trim(),
                                "",
                                contactNumber.value!!.trim(),
                                "",
                                "",
                                "",
                                "",
                                selectedItemContactCode.get()!!,
                                    birthPlaceCountryCode.value!!,
                                /*World.getCountryFrom(birthPlaceCountryCode.value).alpha3,*/
                                gender.name,
                                "",
                                dob.value,
                                dobTime.value,
                                "",
                                "",
                                "",
                                "",
                                    null,
                                "",
                                "N"
                            )


                            signUpRepositary.saveUser(user)
                            val getAllIdentifierType = signUpRepositary.getAllIdentifierType()
                            if (getAllIdentifierType.isNullOrEmpty()){
                                getIdentifierType()
                            }else{
                                listener?.onSuccess("success")
                            }


                        }else{
                            listener?.onShowToast("Sorry! Invalid Birth Time")
                        }
                    }else{
                        listener?.onShowToast("Sorry! Invalid Date of Birth")
                    }
                } else {
                    listener?.onShowToast("Your Email Address 1 is Invalid")
                }
            } else {
                listener?.onShowToast("Email Mismatch")
            }
        }else{
            listener?.onShowToast("All Field(s) Mandatory")
        }

    }
}