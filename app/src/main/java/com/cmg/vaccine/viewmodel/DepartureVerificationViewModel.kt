package com.cmg.vaccine.viewmodel

import android.os.Build
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.TestCodes
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.WorldPriority
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.MASResponseStatic
import com.cmg.vaccine.model.request.WebCheckInReq
import com.cmg.vaccine.model.request.WebCheckInReqData
import com.cmg.vaccine.repositary.DepartureVerificationRepositary
import com.cmg.vaccine.util.*
import com.google.android.gms.common.api.ApiException
import immuniteeEncryption.EncryptionUtils
import io.paperdb.Paper
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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


    var hours:Long = 72
    var listener:SimpleListener?=null

    fun loadData(){
        //val userData = repositary.getUserData()
        var maxDate:Date?=null
        val userData = Paper.book().read<Dashboard>(Passparams.CURRENT_USER_SUBSID,null)
        if (userData != null){
            if (!userData.fullName.isNullOrEmpty()){
                fullName.value = userData.fullName
            }

            if (!userData.passportNo.isNullOrEmpty()){
                passportNo.value = userData.passportNo
            }

            if (!userData.idNo.isNullOrEmpty()){
                idNo.value = userData.idNo
            }
        }


        if (!qrCodeValue.value.isNullOrEmpty()){

            try {

                val jsonObject = JSONObject(qrCodeValue.value)

                if (jsonObject.has("purpose")){
                    if (jsonObject.getString("purpose").equals("Counter Check-In",false)){
                        if (jsonObject.has("data")) {
                            val getData = jsonObject.getString("data")
                            val decryptData = EncryptionUtils.decryptBackupKey(getData,repositary.getCounterCheckinDecryptKey(Passparams.COUNTER_CHECKIN))
                            val data = JSONObject(decryptData)

                            departureDestination.value = data.getString("reqDepatureDestination")
                            arrivalDestination.value = data.getString("reqArrivalDestination")

                            etdTime.value = data.getString("reqEtdTime")
                            etaTime.value = data.getString("reqEtaTime")
                            staffName.value = data.getString("reqStaffName")
                            flightNo.value = data.getString("reqFlightNo")
                            airLine.value = data.getString("reqAirline")

                            val etdDate = changeDateFormatBC(data.getString("reqEtdTime"))
                            val etaDate = changeDateFormatBC(data.getString("reqEtaTime"))

                            val edtDateArray = etdDate?.split(" ")

                            if (edtDateArray?.size!! > 1) {
                                departureDate.value = changeDateFormatForViewProfile(edtDateArray?.get(0)!!)
                                //departureTime.value = removeSeconds(edtDateArray[1])
                                departureTime.value = edtDateArray[1]
                            }else{
                                departureDate.value = etdDate
                            }

                            val etaDateArray = etaDate?.split(" ")

                            if (etaDateArray?.size!! > 1) {
                                arrivalDate.value = changeDateFormatForViewProfile(etaDateArray?.get(0)!!)
                                //arrivalTime.value = removeSeconds(etaDateArray[1])
                                arrivalTime.value = etaDateArray[1]
                            }else{
                                arrivalDate.value = etaDate
                            }



                            val getAirportValues = repositary.getAirportCityByCode(arrivalDestination.value!!)

                            if (getAirportValues != null) {
                                if (!getAirportValues.countryCode.isNullOrEmpty()){
                                    val worldEntryRule = repositary.getJoinWorldEntryRuleAndPriority(getAirportValues.countryCode!!)



                                    var listTestReportFilterByHours:ArrayList<TestReport> = ArrayList()

                                    var observationCode = ArrayList<String>()

                                    val testReport = repositary.getTestReportList(userData.privateKey!!)

                                    worldEntryRule.forEach {
                                        when(it.woen_rule_match_criteria){
                                            "T" ->{
                                                if (it.prioRuleCriteria.equals("Mandatory",false)) {
                                                    hours = it.woen_duration_hours?.toLong()!!

                                                    observationCode.add(it.woen_test_code!!)
                                                }
                                                if (it.prioRuleCriteria.equals("Selective",false)) {
                                                    hours = it.woen_duration_hours?.toLong()!!
                                                    observationCode.add(it.woen_test_code!!)
                                                }
                                            }
                                        }
                                    }
                                    testReport.forEach {
                                        if ((!it.dateSampleCollected.isNullOrEmpty()) and (!it.timeSampleCollected.isNullOrEmpty())){
                                            val sampleDate = changeDateFormatNewISO8601(it.dateSampleCollected + " " + it.timeSampleCollected + ":00")
                                            val calculateHours = calculateHours(changeDateToTimeStamp(etdTime.value!!)!!,changeDateToTimeStamp(sampleDate!!)!!)
                                            if (calculateHours != null){
                                                if (calculateHours <= hours) {
                                                    listTestReportFilterByHours.add(it)
                                                }

                                            }
                                        }
                                    }

                                    var testCodesFilterByTestReport = ArrayList<TestCodes>()

                                    val testCodes = repositary.getTestCodesByCategory(observationCode,getAirportValues.countryCode!!)
                                    for (i in testCodes.indices){
                                        for (j in listTestReportFilterByHours.indices){
                                            if (testCodes[i].wetstTestCode.equals(listTestReportFilterByHours[j].testCode)){
                                                testCodesFilterByTestReport.add(testCodes[i])
                                            }
                                        }
                                    }

                                    if ((!listTestReportFilterByHours.isNullOrEmpty()) and (!testCodesFilterByTestReport.isNullOrEmpty())){
                                        for (i in testCodesFilterByTestReport.indices){
                                            for (j in listTestReportFilterByHours.indices){
                                                if (!listTestReportFilterByHours[j].observationCode.isNullOrEmpty()){
                                                    val observationStatusCode = testCodesFilterByTestReport[i].wetstObservationStatusCode
                                                    val observationStatusCodeArray = observationStatusCode?.split("|")
                                                    val observationTrimArray = ArrayList<String>()
                                                    for (k in observationStatusCodeArray!!.indices){
                                                        observationTrimArray.add(observationStatusCodeArray[k].trim())
                                                    }
                                                    if (observationTrimArray?.contains(listTestReportFilterByHours[j].observationCode!!)){
                                                        status.set(true)
                                                        return
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    status.set(false)
                                }else{
                                    listener?.onShowToast("Database Error, Country Code return Null or Empty")
                                    status.set(false)
                                }

                            }else{
                                listener?.onFailure("2Please Sync your Data, your TestReport Empty")
                                status.set(false)
                            }

                        }
                    }else if (jsonObject.getString("purpose").equals("Web Check-in",false)){
                        if (jsonObject.has("data")){
                            listener?.onStarted()
                            val getData = jsonObject.getString("data")
                            val decryptData = EncryptionUtils.decryptBackupKey(getData,repositary.getCounterCheckinDecryptKey(Passparams.WEB_CHECKIN))
                            Log.d("decrypt",decryptData)
                            val data = JSONObject(decryptData)
                            //listener?.onSuccess("Ecode is missing")

                            var passengerIdno = ""
                            var passengerPassportNo = ""
                            var passengerPassportExpiryDate=""

                            if (data.has("reqPassengerIdNo")){
                                passengerIdno = data.getString("reqPassengerIdNo")
                            }

                            if (data.has("reqPassengerPassportNo")){
                                passengerPassportNo = data.getString("reqPassengerPassportNo")
                            }

                            if (data.has("reqPassengerExpiry")){
                                passengerPassportExpiryDate = data.getString("reqPassengerExpiry")
                            }


                            val webCheckInReq = WebCheckInReq()
                            val webCheckInReqData = WebCheckInReqData(
                                data.getString("dobEcode"),
                                changeDateFormatOnlyDateReverse(data.getString("reqPassengerDob"))!!,
                                passengerIdno,
                                data.getString("reqPassengerIdType"),
                                data.getString("reqPassengerName"),
                                passengerPassportNo,
                                passengerPassportExpiryDate,
                                userData.privateKey!!,
                                userData.subId!!
                            )
                            webCheckInReq.data = webCheckInReqData
                            webCheckInAPI(webCheckInReq)
                        }
                    }
                }



            }catch (e:JSONException){
                listener?.onShowToast(e.message!!)
            }catch (e:Exception){
                listener?.onShowToast(e.message!!)
            }

        }
    }

    private fun webCheckInAPI(webCheckInReq: WebCheckInReq){
        Couritnes.main {
            try {
                val response = repositary.webCheckInApi(webCheckInReq)
                if (response.StatusCode == 1){
                    listener?.onSuccess(response.Message)
                }else{
                    listener?.onFailure("2"+response.Message)
                }
            }catch (e:ApiException){
                listener?.onShowToast("2"+e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure("3"+e.message!!)
            }catch (e:Exception){
                listener?.onShowToast("2"+e.message!!)
            }
        }

    }
}