package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.model.MASResponseStatic
import com.cmg.vaccine.repositary.DepartureVerificationRepositary
import com.cmg.vaccine.util.changeDateFormatForViewProfile
import com.cmg.vaccine.util.removeSeconds
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

class DepartureVerificationViewModel(
    private val repositary: DepartureVerificationRepositary
): ViewModel(){

    var qrCodeValue:MutableLiveData<String> = MutableLiveData()

    var fullName:MutableLiveData<String> = MutableLiveData()
    var passportNo:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()

    var departureDestination:MutableLiveData<String> = MutableLiveData()
    var arrivalDestination:MutableLiveData<String> = MutableLiveData()
    var etdTime:MutableLiveData<String> = MutableLiveData()
    var etaTime:MutableLiveData<String> = MutableLiveData()
    var staffName:MutableLiveData<String> = MutableLiveData()
    var flightNo:MutableLiveData<String> = MutableLiveData()
    var airLine:MutableLiveData<String> = MutableLiveData()

    var departureDate:MutableLiveData<String> = MutableLiveData()
    var arrivalDate:MutableLiveData<String> = MutableLiveData()
    var departureTime:MutableLiveData<String> = MutableLiveData()
    var arrivalTime:MutableLiveData<String> = MutableLiveData()

    var status = ObservableBoolean()

    fun loadData(){
        val userData = repositary.getUserData()
        if (userData != null){
            if (!userData.fullName.isNullOrEmpty()){
                fullName.value = userData.fullName
            }

            if (!userData.passportNumber.isNullOrEmpty()){
                passportNo.value = userData.passportNumber
            }

            if (!userData.patientIdNo.isNullOrEmpty()){
                idNo.value = userData.patientIdNo
            }
        }


        if (!qrCodeValue.value.isNullOrEmpty()){

            try {

                val jsonObject = JSONObject(qrCodeValue.value)
                if (jsonObject.has("status")) {
                    if (jsonObject.getString("status").equals("yes", false)) {
                        status.set(true)
                    } else {
                        status.set(false)
                    }
                }
                if (jsonObject.has("data")) {
                    val data = jsonObject.getJSONObject("data")
                    departureDestination.value = data.getString("reqDepatureDestination")
                    arrivalDestination.value = data.getString("reqArrivalDestination")
                    etdTime.value = data.getString("reqEtdTime")
                    etaTime.value = data.getString("reqEtaTime")
                    staffName.value = data.getString("reqStaffName")
                    flightNo.value = data.getString("reqFlightNo")
                    airLine.value = data.getString("reqAirline")

                    val etdDate = data.getString("reqEtdTime")
                    val etaDate = data.getString("reqEtaTime")

                    val edtDateArray = etdDate.split(" ")

                    if (edtDateArray.size > 1) {
                        departureDate.value = changeDateFormatForViewProfile(edtDateArray[0])
                        departureTime.value = removeSeconds(edtDateArray[1])
                    }else{
                        departureDate.value = etdDate
                    }

                    val etaDateArray = etaDate.split(" ")

                    if (etaDateArray.size > 1) {
                        arrivalDate.value = changeDateFormatForViewProfile(etaDateArray[0])
                        arrivalTime.value = removeSeconds(etaDateArray[1])
                    }else{
                        arrivalDate.value = etaDate
                    }
                }

            }catch (e:JSONException){
                e.printStackTrace()
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
}