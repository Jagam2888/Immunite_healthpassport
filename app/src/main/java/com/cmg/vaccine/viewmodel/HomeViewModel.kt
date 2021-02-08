package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.response.HomeResponse
import com.cmg.vaccine.repositary.HomeRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import java.net.SocketTimeoutException

class HomeViewModel(
        private val repositary: HomeRepositary
):ViewModel() {

    val _list:MutableLiveData<List<HomeResponse>> = MutableLiveData()

    val list:LiveData<List<HomeResponse>>
    get() = _list

    fun setList(value:List<HomeResponse>){
        _list.value = value
    }

    var listener:SimpleListener?=null

    val fullName:MutableLiveData<String> = MutableLiveData()
   // val lastName:MutableLiveData<String> = MutableLiveData()
    val passportNumber:MutableLiveData<String> = MutableLiveData()
    val gender:MutableLiveData<String> = MutableLiveData()
    val country:MutableLiveData<String> = MutableLiveData()
    val _privateKey:MutableLiveData<String> = MutableLiveData()

    var isVaccine = ObservableBoolean()

    val brandName:MutableLiveData<String> = MutableLiveData()
    val vaccineDate:MutableLiveData<String> = MutableLiveData()
    val expiryDate:MutableLiveData<String> = MutableLiveData()

    //var listDashboard:List<Dashboard>?=null

    var _listDashboard:MutableLiveData<List<Dashboard>> = MutableLiveData()
    val listDashboard:LiveData<List<Dashboard>>
    get() = _listDashboard

    init {
        val userData = repositary.getUserData()

        if (userData != null) {
            fullName.value = userData.fullName
            if (userData.gender == "M") {
                gender.value = "MALE"
            }else if (userData.gender == "F"){
                gender.value = "FEMALE"
            }else{
                gender.value = "OTHERS"
            }
            passportNumber.value = userData.passportNumber
            country.value = userData.countryCode
        }

        val dashBoard = Dashboard()
        dashBoard.fullName = userData.fullName
        dashBoard.gender = gender.value
        dashBoard.passportNo = userData.passportNumber
        dashBoard.location = userData.countryCode
        dashBoard.privateKey = userData.privateKey
        _listDashboard.value = listOf(dashBoard)

        val dependentList = repositary.getDependentList()
        if (!dependentList.isNullOrEmpty()) {
            for (dependent in dependentList!!) {
                val dashboard1 = Dashboard()
                dashboard1.fullName = dependent.firstName
                dashboard1.passportNo = dependent.passportNo
                if (dependent.gender == "M") {
                    dashboard1.gender = "MALE"
                } else if (dependent.gender == "F") {
                    dashboard1.gender = "FEMALE"
                } else {
                    dashboard1.gender = "OTHERS"
                }
                dashboard1.privateKey = dependent.childPrivateKey
                dashboard1.relationShip = dependent.relationship
                _listDashboard.value = _listDashboard.value!!.plus(dashboard1)
            }
        }


    }

    fun loadVaccineDetail(){
        listener?.onStarted()
        Couritnes.main {
            try {
                _privateKey.value = repositary.getPrivateKey()
                val response = repositary.getVaccineInfo(_privateKey.value!!)
                if (!response.data.privateKey.isNullOrEmpty()){
                    vaccineDate.value = response.data.vaccineDate
                    expiryDate.value = response.data.item_expiry
                    brandName.value = response.data.brandName
                    isVaccine.set(true)


                }else{
                    isVaccine.set(false)
                }
            }catch (e: APIException){
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }
        }
    }
}