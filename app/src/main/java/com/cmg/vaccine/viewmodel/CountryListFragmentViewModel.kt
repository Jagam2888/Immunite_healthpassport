package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.CountryListFragmentRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import java.lang.Exception

class CountryListFragmentViewModel(
        private val repositary:CountryListFragmentRepositary
):ViewModel() {

    var _countryList:MutableLiveData<List<Countries>> = MutableLiveData()
    val countryList:LiveData<List<Countries>>
    get() = _countryList

    var listener:SimpleListener?=null

    init {
        val getAllCountry = repositary.getAllCountries()

        if (!getAllCountry.isNullOrEmpty()){
            _countryList.value = getAllCountry
        }else{
            getAllCountries()
        }
    }

    private fun getAllCountries(){
        listener?.onStarted()
        Couritnes.main {
            try {
                val response = repositary.getCountriesFromAPI()
                if (!response.data.isNullOrEmpty()){
                    response.data.forEach {
                        val countries = Countries(
                                it.countryCodeAlpha,
                                it.countryMstrSeqno,
                                it.countryName
                        )
                        repositary.insertCountries(countries)
                    }

                    val getAllCountry = repositary.getAllCountries()

                    if (!getAllCountry.isNullOrEmpty()){
                        _countryList.value = getAllCountry
                    }

                }
                listener?.onSuccess("success")

            }catch (e: APIException){
                listener?.onShowToast(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: Exception){
                listener?.onShowToast(e.message!!)
            }
        }
    }
}