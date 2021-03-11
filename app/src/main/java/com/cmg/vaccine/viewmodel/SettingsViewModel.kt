package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.SettingsRepositary
import com.cmg.vaccine.util.*
import com.google.gson.JsonObject
import org.json.JSONObject
import java.net.SocketTimeoutException

class SettingsViewModel(
    private val repositary: SettingsRepositary
):ViewModel() {

    val _loginPinEnable:MutableLiveData<String> = MutableLiveData()
    val loginPinEnable:LiveData<String>
    get() = _loginPinEnable

    var dob = ObservableField<String>()


    var listener:SimpleListener?=null

    init {
        val getLoginPin = repositary.getLoginPin()
        if (getLoginPin != null){
            _loginPinEnable.value = getLoginPin.enable
        }

        val user = repositary.getUserData()
        if (user != null){
            dob.set(changeDateFormatForPrivateKeyDecrypt(user.dob!!))
        }


    }

    fun disableLoginPin(){
        val loginPin = repositary.getLoginPin()
        if (loginPin != null) {
            loginPin.enable = "N"
            repositary.updateLoginPin(loginPin)
        }


    }

    private fun getVaccineTestRef(){
        //listener?.onStarted()
        var privateKey = repositary.getPrivateKey()
        //var privateKey = "CB28F1B9F2BE4C115D42E725FD92514A99AB2E70175B794D47CC76F5CB68A424"

        Couritnes.main {
            try {
                val response = repositary.getVaccineTestRef(privateKey!!)
                if (response != null){
                    val jsonBody = JSONObject(response.string())
                    val jsonBodyData = jsonBody.getJSONArray("data")
                    if (jsonBodyData.length() > 0) {
                        for (i in 0 until jsonBodyData.length()) {
                            var sampleCollectedDate = ""
                            var sampleCollectedTime = "00:00:00"
                            var testCode = ""
                            var qualificationIdentifier = ""
                            var qualificationIssuerName = ""
                            var phone = ""
                            var workAddress = ""
                            var performerName = ""
                            var type = ""
                            var testBy = ""
                            var takenBy = ""
                            var displayName=""
                            var specimenCode=""
                            var specimenName=""
                            var dateSampleCollected=""
                            var dateSampleReceived=""
                            var issueDate=""
                            var statusFinalized=""
                            var testCodeName=""
                            var observationCode=""
                            var observationResult=""

                            val jsonArrayBody = jsonBodyData.getJSONObject(i)
                            if (jsonArrayBody.has("specimen")) {
                                val jsonSpecimen = jsonArrayBody.getJSONObject("specimen")
                                val jsonspecimenType = jsonSpecimen.getJSONObject("type")
                                val jsonSpecimenCoding = jsonspecimenType.getJSONArray("coding")

                                val codeArray = jsonSpecimenCoding.getJSONObject(0)
                                specimenCode = codeArray.getString("code")
                                specimenName = codeArray.getString("display")
                                dateSampleCollected = jsonSpecimen.getString("collectedDateTime")

                                val jsonspecimenMethod = jsonSpecimen.getJSONObject("method")
                                val jsonSpecimenMethodCoding = jsonspecimenMethod.getJSONArray("coding")

                                val jsonSpecimenMethodCodingArray = jsonSpecimenMethodCoding.getJSONObject(0)
                                displayName = jsonSpecimenMethodCodingArray.getString("display")

                                if (!dateSampleCollected.isNullOrEmpty()) {
                                    val isoFormat = changeDateFormatBC(dateSampleCollected)
                                    var dobFormatArray = isoFormat?.split(" ")
                                    sampleCollectedDate = dobFormatArray?.get(0).toString()
                                    sampleCollectedTime = dobFormatArray?.get(1).toString()
                                    //sampleCollectedDate = dateSampleCollected
                                }

                                val jsonSpecimenCollection = jsonSpecimen.getJSONObject("collection")
                                val jsonSpecimenCollectionCollector = jsonSpecimenCollection.getJSONObject("collector")

                                if (jsonSpecimenCollectionCollector.has("name")){
                                    takenBy = jsonSpecimenCollectionCollector.getString("name")
                                }

                                if (jsonSpecimen.has("receivedTime"))
                                    if (!jsonSpecimen.getString("receivedTime").isNullOrEmpty() and !jsonSpecimen.getString("receivedTime").equals("null")){
                                        val isoFormat = changeDateFormatBC(jsonSpecimen.getString("receivedTime"))
                                        var formatArray = isoFormat?.split(" ")
                                        dateSampleReceived = formatArray?.get(0).toString()
                                    }



                            }

                            if (jsonArrayBody.has("observation")){
                                val jsonObservation = jsonArrayBody.getJSONObject("observation")
                                issueDate = jsonObservation.getString("issued")
                                statusFinalized = jsonObservation.getString("status")
                                val jsonPerformer = jsonObservation.getJSONObject("performer")

                                val jsontestCodeMethod = jsonObservation.getJSONObject("method")
                                val jsontestCodeMethodCoding = jsontestCodeMethod.getJSONArray("coding")

                                val jsontestCodeMethodCodeArray = jsontestCodeMethodCoding.getJSONObject(0)
                                testCodeName = jsontestCodeMethodCodeArray.getString("display")

                                if (jsontestCodeMethodCodeArray.has("code")) {
                                    testCode = jsontestCodeMethodCodeArray.getString("code")
                                }
                                val jsonObservation_interpretation = jsonObservation.getJSONArray("interpretation")
                                val jsonObservation_interpretationArray = jsonObservation_interpretation.getJSONObject(0)
                                val jsonObservation_interpretation_valueCodeableConcept = jsonObservation_interpretationArray.getJSONObject("valueCodeableConcept")
                                val jsonObservation_interpretation_valueCodeableConcept_coding = jsonObservation_interpretation_valueCodeableConcept.getJSONArray("coding")
                                val jsonObservation_interpretation_valueCodeableConcept_codingArray = jsonObservation_interpretation_valueCodeableConcept_coding.getJSONObject(0)
                                observationCode = jsonObservation_interpretation_valueCodeableConcept_codingArray.getString("code")
                                observationResult = jsonObservation_interpretation_valueCodeableConcept_codingArray.getString("display")

                                val jsonPerformerName = jsonPerformer.getJSONArray("name")
                                val jsonPerformerNameArray = jsonPerformerName.getJSONObject(0)

                                if (jsonPerformerNameArray.has("text")) {
                                    performerName = jsonPerformerNameArray.getString("text")
                                }

                                val jsonQualification = jsonPerformer.getJSONArray("qualification")
                                if (jsonQualification.length() > 0) {
                                    val jsonQualificationArray = jsonQualification.getJSONObject(0)
                                    if (jsonQualificationArray.has("identifier")) {
                                        qualificationIdentifier = jsonQualificationArray.getString("identifier")
                                    }
                                    if (jsonQualificationArray.has("issuer")) {
                                        qualificationIssuerName = jsonQualificationArray.getString("issuer")
                                    }
                                }

                                val jsonOrganization = jsonPerformer.getJSONObject("organization")
                                val jsonOrganizationContact = jsonOrganization.getJSONObject("contact")

                                if (jsonOrganizationContact.has("telecom")) {

                                    val jsonTelecom = jsonOrganizationContact.getJSONArray("telecom")
                                    if (jsonTelecom.length() > 0) {
                                        val jsonTelecomArray = jsonTelecom.getJSONObject(0)
                                        if (jsonTelecomArray.has("value")) {
                                            phone = jsonTelecomArray.getString("value")
                                        }

                                    }
                                }

                                if (jsonOrganizationContact.has("address")) {

                                    val jsonAddress = jsonOrganizationContact.getJSONObject("address")
                                    if (jsonAddress.has("text")) {
                                        workAddress = jsonAddress.getString("text")
                                    }
                                }

                                if (jsonOrganization.has("type")){
                                    type = jsonOrganization.getString("type")
                                }

                                if (jsonOrganization.has("name")){
                                    testBy = jsonOrganization.getString("name")
                                }
                            }

                            val testReport = TestReport(
                                    privateKey,
                                    displayName,
                                    dateSampleReceived,
                                    specimenCode,
                                    specimenName,
                                    sampleCollectedDate,
                                    sampleCollectedTime,
                                    testCode,
                                    testCodeName,
                                    observationCode,
                                    observationResult,
                                    issueDate,
                                    statusFinalized,
                                    performerName,
                                    qualificationIdentifier,
                                    qualificationIssuerName,
                                    type,
                                    phone,
                                    workAddress,
                                    testBy,
                                    takenBy,
                                    ""
                            )

                            repositary.insertTestReport(testReport)
                        }
                        listener?.onSuccess(jsonBody.getString("reason"))
                    }else{
                        listener?.onFailure(jsonBody.getString("reason"))
                    }
                }
            }catch (e: APIException) {
                listener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                listener?.onFailure(e.message!!)
            } catch (e: SocketTimeoutException) {
                listener?.onFailure(e.message!!)
            }
        }
    }

    fun enableLoginPin():LoginPin{
        val loginPin = repositary.getLoginPin()

        if (loginPin != null) {
            loginPin.enable = "Y"
            repositary.updateLoginPin(loginPin)
        }
        return loginPin
    }

    fun syncRecord(){
        listener?.onStarted()
        val countVaccine = repositary.deleteVaccine()
        val countTestReport = repositary.deleteTestReport()

        getVaccineTestRef()

        //getVaccineFromAPI()
    }

    private fun getVaccineFromAPI(){
        if (!repositary.getPrivateKey().isNullOrEmpty()) {
            Couritnes.main {
                try {
                    val response = repositary.getVaccineListBlockChain(repositary.getPrivateKey()!!)
                    if (!response.data.data.isNullOrEmpty()) {
                        response.data.data.forEach { vaccineData ->
                            val vaccine = Vaccine(
                                    vaccineData.GITN,
                                    vaccineData.NFCTag,
                                    vaccineData.brandName,
                                    vaccineData.facilityname,
                                    vaccineData.gsicodeSerialCode,
                                    vaccineData.itemBatch,
                                    vaccineData.malNo,
                                    vaccineData.manufacturerName,
                                    vaccineData.manufacturerNo,
                                    vaccineData.recordId,
                                    vaccineData.status,
                                    vaccineData.uuidTagNo,
                                    vaccineData.vaccinetype
                            )
                            repositary.insertVaccine(vaccine)
                        }
                    }
                    getTestReportListFromAPI()
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            listener?.onFailure("Private key not generated yet")
        }
    }

    private fun getTestReportListFromAPI(){
        if (!repositary.getPrivateKey().isNullOrEmpty()) {
            Couritnes.main {
                try {
                    val response = repositary.getTestReportList(repositary.getPrivateKey()!!)
                    if (!response.data.data.isNullOrEmpty()) {
                        response.data.data.forEach { report ->
                            /*val testReport = TestReport(
                                    report.codeDisplay,
                                    report.codeSystem,
                                    report.collectedDateTime,
                                    report.conceptCode,
                                    report.conceptName,
                                    report.contactAddressText,
                                    report.contactAddressType,
                                    report.contactAddressUse,
                                    report.contactTelecom,
                                    report.contactTelecomValue,
                                    report.effectiveDateTime,
                                    report.name,
                                    report.qualificationIssuerName,
                                    report.qualitificationIdentifier,
                                    report.recordId,
                                    report.status,
                                    report.type
                            )
                            repositary.insertTestReport(testReport)*/
                        }

                    }
                    listener?.onSuccess("Sync Successfully")
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            listener?.onFailure("Private key not generated yet")
        }
    }

}