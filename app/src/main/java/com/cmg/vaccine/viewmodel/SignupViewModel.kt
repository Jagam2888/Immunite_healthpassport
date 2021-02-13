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
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.SignUpRepositary
import com.cmg.vaccine.util.*
import com.hbb20.CountryCodePicker
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
    val selectedItemContactCode = ObservableField<String>()
    var selectedItemNationalityCode = ObservableInt()

    var selectedYearsItem = ObservableField<String>()
    var selectedYearsMonth = ObservableField<String>()
    var selectedYearsDay = ObservableField<String>()

    var listener:SimpleListener?=null

    var _countries:MutableLiveData<List<Countries>> = MutableLiveData()

    val countries:LiveData<List<Countries>>
    get() = _countries





    /*var _years:MutableLiveData<List<String>> = MutableLiveData()
    val years:LiveData<List<String>>
    get() = _years

    var _days:MutableLiveData<List<String>> = MutableLiveData()
    val days:LiveData<List<String>>
        get() = _days


    val clicksListenerYears = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedYearsItem.set(parent?.getItemAtPosition(position) as String)
            loadDays()
        }
    }

    val clicksListenerMonth = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedYearsMonth.set(parent?.getItemAtPosition(position) as String)
            loadDays()
        }
    }

    val clicksListenerDay = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedYearsDay.set(parent?.getItemAtPosition(position) as String)
        }
    }

    fun loadYears(){
        var listYaers:List<String>?=null
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        listYaers = listOf("1900")
        for (i in 1901..thisYear) {
            //listYaers = arrayListOf(i.toString())
            listYaers = listYaers?.plus(i.toString())
        }
        _years.value = listYaers
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDays(){
        var currentYear = 1900
        var currentMonth = 1
        var listDays:List<String>?=null
        listDays = listOf("1")

        //val currentYear = selectedYearsItem.get()
        if (!selectedYearsItem.get().isNullOrEmpty()){
            currentYear = selectedYearsItem.get()!!.toInt()
        }

        if (!selectedYearsMonth.get().isNullOrEmpty()){
            currentMonth = selectedYearsMonth.get()!!.toInt()
        }

        val yearMonth = YearMonth.of(currentYear,currentMonth)
        val day = yearMonth.lengthOfMonth()

        for (i in 2..day) {
            listDays = listDays?.plus(i.toString())
        }
        _days.value = listDays
    }*/

    var countryList:List<Countries>?=null

    fun setCurrentCountry(country:String){
        countryList = signUpRepositary.getAllCountriesDB()
        if (!countryList.isNullOrEmpty()){
            val pos = selectedCurrentCountry(country,countryList!!)
            selectedItemNationalityCode.set(pos)
            //selectedItemNationalityCode.set(5)
        }
    }

    init {
        listener?.onStarted()
        countryList = signUpRepositary.getAllCountriesDB()
        if (countryList.isNullOrEmpty()){
            Couritnes.main {
                try {
                    val reponse = signUpRepositary.getAllCountries()
                    if (reponse.data.isNotEmpty()){
                        for (country in reponse.data){
                            val countries = Countries(
                                    country.countryCodeAlpha,
                                    country.countryMstrSeqno,
                                    country.countryName

                            )
                            signUpRepositary.insertCountries(countries)
                        }
                        countryList = signUpRepositary.getAllCountriesDB()
                        _countries.value = countryList
                        listener?.onSuccess("")
                    }else{
                        listener?.onFailure("Failed to load Countries")
                    }
                }catch (e:APIException){
                    listener?.onFailure(e.message!!)
                }catch (e:NoInternetException){
                    listener?.onFailure(e.message!!)
                }catch (e:SocketTimeoutException){
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            listener?.onSuccess("success")
            _countries.value = countryList
        }
    }

    fun onSignUp(){
        listener?.onStarted()
        var placeBirth = ""
        if (!countryList.isNullOrEmpty()){
            placeBirth = countryList?.get(selectedItemNationalityCode.get())?.countryName!!
        }
        if(!fullName.value.isNullOrEmpty()and !email.value.isNullOrEmpty()) {
            if (email.value.equals(reTypeEmail.value)) {
                if (isValidEmail(email.value!!)) {
                        /*if (selectedItemIDType.get() == 0) {
                            idType = "MyKad"
                        } else {
                            idType = "Passport"
                        }*/

                        /*var user = User(
                            "",
                            1,
                            System.currentTimeMillis(),
                            email1!!,
                            email2!!,
                            firstName!!,
                            "M",
                            lastName!!,
                            "123455",
                            idNumber!!,
                            idType!!,
                            1,
                            "",
                            password!!,
                            1,
                            System.currentTimeMillis()
                        )*/
                            //dob.value = "${selectedYearsDay.get()}/${selectedYearsMonth.get()}/${selectedYearsItem.get()} ${dobTime.value}"
                            dob.value = "${dob.value} ${dobTime.value}"
                            var user = User(
                                    0,
                                    fullName.value!!,
                                    "",
                                    email.value!!,
                                    "",
                                    contactNumber.value!!,
                                    "",
                                    "",
                                    selectedItemContactCode.get()!!,
                                    placeBirth,
                                    gender.name,
                                    "MY",
                                    dob.value!!,
                                    "",
                                    "",
                                    "",
                                    "",
                                    0,
                                    System.currentTimeMillis(),
                                    0,
                                    System.currentTimeMillis(),
                                    "",
                                    "N")

                        signUpRepositary.saveUser(user)
                        listener?.onSuccess("success")
                } else {
                    listener?.onFailure("Your Email Address 1 is Invalid")
                }
            } else {
                listener?.onFailure("Email Mismatch")
            }
        }else{
            listener?.onFailure("All Field(s) Mandatory")
        }

    }
}