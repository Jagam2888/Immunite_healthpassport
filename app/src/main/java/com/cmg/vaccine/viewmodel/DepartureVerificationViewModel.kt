package com.cmg.vaccine.viewmodel

import android.os.Build
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.TestCodes
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.WorldPriority
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.MASResponseStatic
import com.cmg.vaccine.repositary.DepartureVerificationRepositary
import com.cmg.vaccine.util.*
import immuniteeEncryption.EncryptionUtils
import io.paperdb.Paper
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception
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
                /*if (jsonObject.has("status")) {
                    if (jsonObject.getString("status").equals("yes", false)) {
                        status.set(true)
                    } else {
                        status.set(false)
                    }
                }*/
                if (jsonObject.has("data")) {
                    val getData = jsonObject.getString("data")
                    val decryptData = EncryptionUtils.decryptBackupKey(getData,"20210327")
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
                        val worldEntryRule = repositary.getWorldEnteryRuleByCountry(getAirportValues.countryCode!!,"T")

                        var worldPriorityList:ArrayList<WorldPriority> = ArrayList()
                        var testCodesList:ArrayList<TestCodes> = ArrayList()
                        var listRuleSeqNo:ArrayList<String> = ArrayList()
                        var listTestCodeByCategory:ArrayList<String> = ArrayList()
                        var listTestReportByTestCode:ArrayList<TestReport> = ArrayList()
                        worldEntryRule.forEach {
                            val worldPriority = repositary.getWorldPriorityByRuleNo(
                                it.woen_rule_seq_no!!,
                                it.woen_country_code!!
                            )
                            if (worldPriority != null) {
                                worldPriorityList.add(worldPriority)
                                listRuleSeqNo.add(it.woen_rule_seq_no!!)

                                val testCodes = repositary.getTestCodesByCategory(
                                    it.woen_test_code!!,
                                    it.woen_country_code!!
                                )
                                if (!testCodes.isNullOrEmpty()) {
                                    testCodes.forEach { testCode ->
                                        listTestCodeByCategory.add(testCode.wetstTestCode!!)

                                        val testReport =
                                            repositary.getTestReportByTestCode(testCode.wetstTestCode!!)
                                        listTestReportByTestCode.add(testReport)
                                    }

                                }
                            }

                            hours = it.woen_duration_hours?.toLong()!!
                            /*when(it.woen_rule_match_criteria){
                            "T" ->{
                                hours = it.woen_duration_hours?.toLong()!!
                            }
                        }*/
                        }
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                        if (!listTestReportByTestCode.isNullOrEmpty()) {
                            if (listTestReportByTestCode.size == 1) {
                                var getSampleCollectedDate =
                                    changeDateFormatNewISO8601(listTestReportByTestCode[0].dateSampleCollected + " " + listTestReportByTestCode[0].timeSampleCollected + ":00")

                                val calculateHours = calculateHours(
                                    changeDateToTimeStamp(etdTime.value!!)!!,
                                    changeDateToTimeStamp(getSampleCollectedDate!!)!!
                                )

                                if (calculateHours != null) {
                                    if (calculateHours <= hours) {
                                        status.set(true)
                                    } else {
                                        status.set(false)
                                    }
                                }
                            } else {
                                val listDate: ArrayList<Date> = ArrayList()
                                listTestReportByTestCode.forEach {
                                    val date =
                                        dateFormat.parse(it.dateSampleCollected + " " + it.timeSampleCollected)
                                    listDate.add(date)
                                }

                                maxDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    listDate.stream().max(Date::compareTo).get()
                                } else {
                                    Collections.max(listDate)
                                }

                                var getSampleCollectedDate =
                                    changeDateFormatNewISO8601(dateFormat.format(maxDate) + ":00")

                                val calculateHours = calculateHours(
                                    changeDateToTimeStamp(etdTime.value!!)!!,
                                    changeDateToTimeStamp(getSampleCollectedDate!!)!!
                                )

                                if (calculateHours != null) {
                                    if (calculateHours <= hours) {
                                        status.set(true)
                                    } else {
                                        status.set(false)
                                    }
                                }

                            }
                        }
                    }else{
                        listener?.onFailure("Please Sync your Data, your TestReport Empty")
                        status.set(false)
                    }
                    /*if (!userData.privateKey.isNullOrEmpty()) {
                        var getTestReport = repositary.getTestReportList(userData.privateKey!!)

                        var getSampleCollectedDate = changeDateFormatNewISO8601(getTestReport[0].dateSampleCollected+" "+getTestReport[0].timeSampleCollected+":00")

                        val calculateHours = calculateHours(changeDateToTimeStamp(etdTime.value!!)!!, changeDateToTimeStamp(getSampleCollectedDate!!)!!)

                        if (calculateHours != null) {
                            if (calculateHours <= hours){
                                status.set(true)
                            }else{
                                status.set(false)
                            }
                        }
                    }*/



                }

            }catch (e:JSONException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }

        }
    }
}