package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.*
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.SettingsRepositary
import com.cmg.vaccine.util.*
import com.google.gson.JsonObject
import io.reactivex.Observable
import org.json.JSONObject
import java.lang.Exception
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

    fun syncRecord(){
        listener?.onStarted()
        repositary.deleteVaccine()
        repositary.deleteTestReport()
        repositary.deleteWorldEntryCountries()
        repositary.deleteAllAirportCities()
        repositary.deleteAllWorldEntryRuleByCountry()
        repositary.deleteIdentifierType()
        repositary.deleteTestCodes()
        repositary.deleteWorldPriority()
        repositary.deleteAllCountries()


        getAllCountries()
        getWorldPriorities()
        getIdentifierType()
        getAllTestCodes()
        getAllWorldEntryCountryRules()
        getWorldEntryCountries()
        getAllAirportCities()
        getVaccineCall()


    }

    private fun getAllCountries(){
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

                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun getVaccineCall(){
        getVaccineTestRef(repositary.getPrivateKey()!!,"Prinicipal")
        //getVaccineTestRef("5874C4015510CE5786D9CFD40F14B7BB89E996DC0D8BF29DAFE875DF8D532536","Prinicipal")

        val dependent = repositary.getAllDependent()

        if (!dependent.isNullOrEmpty()){
            dependent.forEach {
                if (!it.privateKey.isNullOrEmpty()){
                    getVaccineTestRef(it.privateKey!!,"Dependent ${it.firstName}")
                    //getVaccineTestRef("5B9137D189F408B754C75E84F3C60FA92FE098173D60D6516D1233EC672A6475","Dependent ${it.firstName}")
                }
            }
        }

        //getVaccineFromAPI()
    }

    private fun getWorldPriorities(){
        Couritnes.main {
            try {
                val response = repositary.getWorldPriority()
                if (!response.data.isNullOrEmpty()){
                    response.data.forEach {
                        val worldPriority = WorldPriority(
                            it.prioRuleCountry,
                            it.prioRuleCriteria,
                            it.prioRuleNo,
                                it.prioRulePair,
                            it.prioSeqNo
                        )
                        repositary.insertWorldPriority(worldPriority)
                    }
                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun getAllTestCodes(){
        Couritnes.main {
            try {
                val response = repositary.getTestCodeFromApi()
                if (!response.data.isNullOrEmpty()){
                    response.data.forEach {
                        val testCodes = TestCodes(
                            it.wetstCountryCode,
                            it.wetstSeqNo,
                            it.wetstTestCode,
                                it.wetstObservationStatusCode,
                            it.wetstTestcategory

                        )
                        repositary.insertTestCodes(testCodes)
                    }
                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
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

                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun getAllAirportCities(){
        Couritnes.main {
            try {
                val getAllAirportCities = repositary.getAllAirportCities()
                if ((getAllAirportCities != null) and (getAllAirportCities.data.isNotEmpty())){
                    getAllAirportCities.data.forEach {
                        val airportCitiesName = AirportCitiesName(
                                it.airportName,
                                it.cityCode,
                                it.countryCode,
                                it.countryName,
                                it.id
                        )
                        repositary.insertAirportCitiesMaster(airportCitiesName)
                    }
                }
                //getWorldEntryCountries()
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }

        }
    }

    private fun getAllWorldEntryCountryRules(){
        Couritnes.main {
            try {
                val worlentryRules = repositary.getAllWorldEntryCountryRules()
                if (!worlentryRules.data.isNullOrEmpty()){
                    worlentryRules.data.forEach {data->
                        val worldEntryRulesByCountry = WorldEntryRulesByCountry(
                            data.woenSeqNo,
                            data.woenCountryCode,
                            data.woenDurationHours,
                            data.woenEnddate,
                            data.woenPoints,
                            data.woenRuleDescription,
                            data.woenRuleMatchCriteria,
                            data.woenRuleSeqNo,
                            data.woenStartdate,
                            data.woenStatus,
                            data.woenTestCode,
                            data.woenVaccineCode

                        )
                        repositary.insertWorldEntryRuleByCountry(worldEntryRulesByCountry)
                    }
                }
                //getAllAirportCities()
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun getWorldEntryCountries(){
        Couritnes.main {
            try {
                val worldEntryCountries = repositary.getWorldEntriesCountryList()
                if (!worldEntryCountries.data.isNullOrEmpty()) {
                    worldEntryCountries.data.forEach { data->
                        val worldEntryCountries = WorldEntryCountries(
                                data.countryName,
                                data.countryCodeAlpha,
                                data.countryMstrSeqno
                        )
                        repositary.insertWorldEntryCountries(worldEntryCountries)
                    }
                    //listener?.onSuccess("")
                }
                //getVaccineCall()
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }

        }
    }

    private fun getVaccineTestRef(privateKey:String,user:String){

        Couritnes.main {
            try {
                val response = repositary.getVaccineTestRef(privateKey)
                if (response != null){
                    val jsonBody = JSONObject(response.string())
                    val jsonBodayDataObject = jsonBody.getJSONObject("data")
                    val jsonBodyData = jsonBodayDataObject.getJSONArray("data")
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
                            var displayName = ""
                            var specimenCode = ""
                            var specimenName = ""
                            var dateSampleCollected = ""
                            var dateSampleReceived = ""
                            var issueDate = ""
                            var statusFinalized = ""
                            var testCodeName = ""
                            var observationCode = ""
                            var observationResult = ""
                            var recordId = ""

                            val jsonArrayBody = jsonBodyData.getJSONObject(i)
                            if ((jsonArrayBody.has("specimen")) and (jsonArrayBody.has("observation"))) {
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
                                        val isoFormat = changeDateFormatNormal(dateSampleCollected)
                                        var dobFormatArray = isoFormat?.split(" ")
                                        if (dobFormatArray?.size!! > 1) {
                                            sampleCollectedDate = dobFormatArray?.get(0).toString()
                                            sampleCollectedTime = dobFormatArray?.get(1).toString()
                                        }
                                        //sampleCollectedDate = dateSampleCollected
                                    }

                                    val jsonSpecimenCollection = jsonSpecimen.getJSONObject("collection")
                                    val jsonSpecimenCollectionCollector = jsonSpecimenCollection.getJSONObject("collector")

                                    if (jsonSpecimenCollectionCollector.has("name")) {
                                        takenBy = jsonSpecimenCollectionCollector.getString("name")
                                    }

                                    /*if (jsonSpecimen.has("receivedTime"))
                                    if (!jsonSpecimen.getString("receivedTime").isNullOrEmpty() and !jsonSpecimen.getString("receivedTime").equals("null")){
                                        val isoFormat = changeDateFormatNormal(jsonSpecimen.getString("receivedTime"))
                                        var formatArray = isoFormat?.split(" ")
                                        dateSampleReceived = formatArray?.get(0).toString()
                                    }*/


                                }

                                if (jsonArrayBody.has("observation")) {
                                    val jsonObservation = jsonArrayBody.getJSONObject("observation")
                                    //issueDate = jsonObservation.getString("issued")
                                    statusFinalized = jsonObservation.getString("status")
                                    val jsonPerformer = jsonObservation.getJSONObject("performer")

                                    val jsontestCodeMethod = jsonObservation.getJSONObject("code")
                                    val jsontestCodeMethodCoding = jsontestCodeMethod.getJSONArray("coding")
                                    if (jsontestCodeMethodCoding.length() > 1) {
                                        val jsontestCodeMethodCodeArray =
                                            jsontestCodeMethodCoding.getJSONObject(1)
                                        testCodeName =
                                            jsontestCodeMethodCodeArray.getString("display")

                                        if (jsontestCodeMethodCodeArray.has("code")) {
                                            testCode = jsontestCodeMethodCodeArray.getString("code")
                                        }
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

                                    if (jsonOrganization.has("type")) {
                                        type = jsonOrganization.getString("type")
                                    }

                                    if (jsonOrganization.has("name")) {
                                        testBy = jsonOrganization.getString("name")
                                    }

                                    if (jsonObservation.has("issued")) {
                                        if (!jsonObservation.getString("issued").isNullOrEmpty() and !jsonObservation.getString("issued").equals("null")) {
                                            val isoFormat = changeDateFormatNormal(jsonObservation.getString("issued"))
                                            var formatArray = isoFormat?.split(" ")
                                            dateSampleReceived = formatArray?.get(0).toString()
                                            issueDate = formatArray?.get(0).toString()
                                        }
                                        //val jsonObservationIssueDate = jsonObservation.getString("issued")
                                    }
                                    if (jsonObservation.has("basedOn")){
                                        val jsonObservationBasedOn = jsonObservation.getJSONObject("basedOn")
                                        recordId = jsonObservationBasedOn.getString("value")
                                    }
                                }

                                val testReport = TestReport(
                                        recordId,
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
                        }
                        listener?.onSuccess(jsonBodayDataObject.getString("reason"))
                    }else{
                        listener?.onFailure(jsonBodayDataObject.getString("reason")+" : "+user)
                    }
                }
            }catch (e: APIException) {
                listener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                listener?.onFailure(e.message!!)
            } catch (e: SocketTimeoutException) {
                listener?.onFailure(e.message!!)
            } catch (e:Exception){
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