package com.cmg.vaccine.viewmodel

import android.os.Build
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.data.WorldEntryRuleData
import com.cmg.vaccine.data.WorldEntryRulePairResult
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



    var listener:SimpleListener?=null

    fun getPurpose():String?{
        var result = ""
        try {
            val jsonObject = JSONObject(qrCodeValue.value)
            if (jsonObject.has("purpose")){
                result = jsonObject.getString("purpose")
            }
        }catch (e:JSONException){
            result = ""
        }

        return result
    }

    fun loadData(){
        //val userData = repositary.getUserData()
        var hours:Int = 0
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
                    if (jsonObject.getString("purpose").equals("Counter Check-In",true)){
                        if (jsonObject.has("data")) {
                            val getData = jsonObject.getString("data")
                            var decryptData = ""
                            decryptData = EncryptionUtils.decryptBackupKey(getData,repositary.getCounterCheckinDecryptKey(Passparams.COUNTER_CHECKIN))
                            if ((decryptData.isNullOrEmpty()) or (decryptData.equals("invalid",true))){
                                decryptData = EncryptionUtils.decryptBackupKey(getData,repositary.getCounterCheckinDecryptKey(Passparams.COUNTER_CHECKIN_2))
                            }
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


                                    var observationCode = ArrayList<String>()
                                    var observationCodeMandatory = ArrayList<WorldEntryRuleData>()
                                    var observationCodeSelective = ArrayList<WorldEntryRuleData>()
                                    var priorRulePairList = ArrayList<String>()
                                    var worldEntryRulePairTotalResult = ArrayList<WorldEntryRulePairResult>()

                                    val testReport = repositary.getTestReportList(userData.privateKey!!)
                                    worldEntryRule.forEach {
                                        when(it.woen_rule_match_criteria){
                                            "T" ->{
                                                if (it.prioRuleCriteria.equals("Mandatory",false)) {
                                                    if (!it.woen_duration_hours.isNullOrEmpty()) {
                                                        hours = it.woen_duration_hours.toInt()
                                                    }
                                                    if (!it.woen_test_code.isNullOrEmpty()) {
                                                        observationCode.add(it.woen_test_code)
                                                    }
                                                    val worlEntryRuleData = WorldEntryRuleData(
                                                        hours,
                                                        it.woen_test_code,
                                                        it.prioRulePair,
                                                        it.prioRuleCriteria
                                                    )
                                                    observationCodeMandatory.add(worlEntryRuleData)
                                                }
                                                if (it.prioRuleCriteria.equals("Selective",false)) {
                                                    if (!it.woen_duration_hours.isNullOrEmpty()) {
                                                        hours = it.woen_duration_hours.toInt()
                                                    }
                                                    if (!it.woen_test_code.isNullOrEmpty()) {
                                                        observationCode.add(it.woen_test_code)
                                                    }
                                                    val worlEntryRuleData = WorldEntryRuleData(
                                                        hours,
                                                        it.woen_test_code,
                                                        it.prioRulePair,
                                                        it.prioRuleCriteria
                                                    )
                                                    observationCodeSelective.add(worlEntryRuleData)
                                                }
                                            }
                                        }
                                    }

                                    val testReportFilterByTestCode = repositary.getTestReportFilterByTestCodes(userData.privateKey!!,getAirportValues.countryCode)
                                    if (testReportFilterByTestCode.isNullOrEmpty()){
                                        status.set(false)
                                    }else {
                                        if (observationCodeMandatory.size > 0) {
                                            if (testReportFilterByTestCode.size >= observationCodeMandatory.size) {
                                                for (i in observationCodeMandatory.indices) {
                                                    for (j in testReportFilterByTestCode.indices) {
                                                        if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())) {
                                                            val sampleDate =
                                                                changeDateFormatNewISO8601(
                                                                    testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00"
                                                                )
                                                            val calculateHours = calculateHours(
                                                                changeDateToTimeStamp(etdTime.value!!)!!,
                                                                changeDateToTimeStamp(sampleDate!!)!!
                                                            )
                                                            if (calculateHours != null) {
                                                                if (calculateHours <= observationCodeMandatory[i].hours) {
                                                                    //listTestReportFilterByHours.add(it)
                                                                    if (i + 1 == observationCodeMandatory.size) {
                                                                        status.set(true)
                                                                    }
                                                                } else {
                                                                    status.set(false)
                                                                }

                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                status.set(false)
                                            }
                                        }
                                        if (observationCodeSelective.size > 0) {
                                            observationCodeSelective.forEach {
                                                if (it.priorRulePair.isNullOrEmpty()) {
                                                    for (j in testReportFilterByTestCode.indices) {
                                                        if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())) {
                                                            val sampleDate =
                                                                changeDateFormatNewISO8601(
                                                                    testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00"
                                                                )
                                                            val calculateHours = calculateHours(
                                                                changeDateToTimeStamp(etdTime.value!!)!!,
                                                                changeDateToTimeStamp(sampleDate!!)!!
                                                            )
                                                            if (calculateHours != null) {
                                                                //val result:Boolean =
                                                                if (calculateHours <= it.hours) {
                                                                    status.set(true)
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (!priorRulePairList.contains(it.priorRulePair)) {
                                                        priorRulePairList.add(it.priorRulePair!!)
                                                    }
                                                }
                                            }
                                            if ((priorRulePairList.size > 0) and (priorRulePairList.size <= testReportFilterByTestCode.size)) {
                                                for (i in priorRulePairList.indices) {
                                                    for (k in observationCodeSelective.indices) {
                                                        if (observationCodeSelective[k].priorRulePair == priorRulePairList[i]) {
                                                            for (j in testReportFilterByTestCode.indices) {
                                                                if ((!testReportFilterByTestCode[j].dateSampleCollected.isNullOrEmpty()) and (!testReportFilterByTestCode[j].timeSampleCollected.isNullOrEmpty())) {
                                                                    val sampleDate =
                                                                        changeDateFormatNewISO8601(
                                                                            testReportFilterByTestCode[j].dateSampleCollected + " " + testReportFilterByTestCode[j].timeSampleCollected + ":00"
                                                                        )
                                                                    val calculateHours =
                                                                        calculateHours(
                                                                            changeDateToTimeStamp(etdTime.value!!)!!,
                                                                            changeDateToTimeStamp(
                                                                                sampleDate!!
                                                                            )!!
                                                                        )
                                                                    if (calculateHours != null) {
                                                                        val rulePairResult =
                                                                            WorldEntryRulePairResult(
                                                                                priorRulePairList[i],
                                                                                calculateHours <= observationCodeSelective[k].hours
                                                                            )
                                                                        worldEntryRulePairTotalResult.add(
                                                                            rulePairResult
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (worldEntryRulePairTotalResult.size > 0) {
                                                    var finalResult = ArrayList<Boolean>()
                                                    for (i in priorRulePairList.indices) {


                                                        for (j in worldEntryRulePairTotalResult.indices) {
                                                            if (worldEntryRulePairTotalResult[j].rulePair == priorRulePairList[i]) {
                                                                val rulePair =
                                                                    WorldEntryRulePairResult(
                                                                        priorRulePairList[i],
                                                                        true
                                                                    )
                                                                if (finalResult.size < i + 1) {
                                                                    if (worldEntryRulePairTotalResult.contains(
                                                                            rulePair
                                                                        )
                                                                    ) {
                                                                        finalResult.add(true)
                                                                    } else {
                                                                        finalResult.add(false)
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    Log.d("priorrule", finalResult.toString())
                                                    status.set(!finalResult.contains(false))
                                                }
                                            } else {
                                                status.set(false)
                                            }
                                        } else {
                                            status.set(false)
                                        }
                                    }


                                    /*var listTestReportFilterByHours:ArrayList<TestReport> = ArrayList()

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
                                    status.set(false)*/
                                }else{
                                    listener?.onShowToast("Database Error, Country Code return Null or Empty")
                                    status.set(false)
                                }

                            }else{
                                listener?.onFailure("2Please Sync your Data, your TestReport Empty")
                                status.set(false)
                            }

                        }
                    }else if (jsonObject.getString("purpose").equals("Web Check-in",true)){
                        if (jsonObject.has("TimeStamp")){
                            var timeStamp = changeDateToTimeStamp(jsonObject.getString("TimeStamp"))
                            if (timeStamp == null){
                                timeStamp = changeDateToTimeStampAlter(jsonObject.getString("TimeStamp"))
                            }
                            val calculateHours = calculateMinutes(System.currentTimeMillis(),timeStamp)
                            if (calculateHours <= 5){
                                if (jsonObject.has("data")){
                                    listener?.onStarted()
                                    val getData = jsonObject.getString("data")
                                    val decryptData = EncryptionUtils.decryptBackupKey(getData,repositary.getCounterCheckinDecryptKey(Passparams.WEB_CHECKIN))
                                    Log.d("decrypt",decryptData)
                                    val data = JSONObject(decryptData)
                                    //listener?.onSuccess("Ecode is missing")

                                    var passengerIdno = ""
                                    var passengerIdType = ""
                                    var passengerPassportNo = ""
                                    var passengerPassportExpiryDate=""

                                    if (!userData.passportNo.isNullOrEmpty()){
                                        passengerPassportNo = userData.passportNo!!
                                    }

                                    if (!userData.passportExpiry.isNullOrEmpty()){
                                        passengerPassportExpiryDate = userData.passportExpiry!!
                                    }

                                    if (!userData.idNo.isNullOrEmpty()){
                                        passengerIdno = userData.idNo!!
                                    }

                                    if (!userData.idType.isNullOrEmpty()){
                                        passengerIdType = userData.idType!!
                                    }

                                    /*if (data.has("reqPassengerIdNo")){
                                        passengerIdno = data.getString("reqPassengerIdNo")
                                    }

                                    if (data.has("reqPassengerPassportNo")){
                                        passengerPassportNo = data.getString("reqPassengerPassportNo")
                                    }

                                    if (data.has("reqPassengerExpiry")){
                                        passengerPassportExpiryDate = data.getString("reqPassengerExpiry")
                                    }*/


                                    val webCheckInReq = WebCheckInReq()
                                    val webCheckInReqData = WebCheckInReqData(
                                        data.getString("dobEcode"),
                                        changeDateFormatOnlyDateReverse(userData.dob!!)!!,
                                        passengerIdno,
                                        passengerIdType,
                                        userData.fullName!!,
                                        passengerPassportNo,
                                        passengerPassportExpiryDate,
                                        userData.privateKey!!,
                                        userData.subId!!
                                    )
                                    webCheckInReq.data = webCheckInReqData
                                    val eCodeDOB = data.getString("dobEcode").dropLast(6)
                                    if (eCodeDOB == changeDateFormatOnlyDateReverse(userData.dob!!)) {
                                        webCheckInAPI(webCheckInReq)
                                    }else{
                                        listener?.onFailure("Sorry! You're not Authorized Person")
                                    }
                                }
                            }else{
                                listener?.onFailure("Sorry!Your eCode is expired")
                            }
                            //Log.d("timestamp",calculateHours.toString())

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