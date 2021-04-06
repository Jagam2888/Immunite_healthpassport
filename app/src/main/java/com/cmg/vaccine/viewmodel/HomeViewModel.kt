package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.DashboardTestData
import com.cmg.vaccine.model.DashboardVaccineData
import com.cmg.vaccine.model.SwitchProfile
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.repositary.HomeRepositary
import com.cmg.vaccine.util.*
import immuniteeEncryption.EncryptionUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
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
    val passportNumber:MutableLiveData<String> = MutableLiveData()
    val country:MutableLiveData<String> = MutableLiveData()
    val _privateKey:MutableLiveData<String> = MutableLiveData()
    val privateKey:LiveData<String>
    get() = _privateKey

    var isVaccine = ObservableBoolean()

    val brandName:MutableLiveData<String> = MutableLiveData()
    val vaccineDate:MutableLiveData<String> = MutableLiveData()
    val expiryDate:MutableLiveData<String> = MutableLiveData()


    var _listDashboard:MutableLiveData<List<Dashboard>> = MutableLiveData()
    val listDashboard:LiveData<List<Dashboard>>
    get() = _listDashboard

    var listDashboardData:List<DashboardVaccineData>?= null
    var listDashboardTestData:List<DashboardTestData>?= null

    var _currentPagerPosition:MutableLiveData<Int> = MutableLiveData()

    val currentPagerPosition:LiveData<Int>
    get() = _currentPagerPosition

    var _users:MutableLiveData<List<SwitchProfile>> = MutableLiveData()

    val users:LiveData<List<SwitchProfile>>
    get() = _users

    var _vaccineList:MutableLiveData<List<Vaccine>> = MutableLiveData()
    val vaccineList:LiveData<List<Vaccine>>
    get() = _vaccineList

    var _testReportList:MutableLiveData<List<TestReport>> = MutableLiveData()
    val testReportList:LiveData<List<TestReport>>
        get() = _testReportList

    //for showing in qrcode screen
    var _userName:MutableLiveData<String> = MutableLiveData()
    val userName:LiveData<String>
        get() = _userName
    var listuser:List<SwitchProfile>?=null


    fun setCurrentItem(position:Int){
        _currentPagerPosition.value = position
    }

    fun setUser(){
        val parent = repositary.getUserData()
        var user = SwitchProfile()

        user.fullName = parent.fullName
        user.tyep = "Principal"
        user.profileImg = parent.profileImage
        listuser = listOf(user)

        val dependent = repositary.getDependentList()

        if (!dependent.isNullOrEmpty()) {
            for (child in dependent!!) {
                var getChild = SwitchProfile()
                getChild.fullName = child.firstName
                getChild.tyep = child.relationship
                getChild.profileImg = child.profileImage
                listuser = listuser?.plus(getChild)
            }
        }

        _users.value = listuser

        if (!parent.privateKey.isNullOrEmpty()){
            _privateKey.value = parent.privateKey
        }
    }


    fun getVaccineTestRef(privateKey:String){

        if (getTestReportList(privateKey).isEmpty()) {
            Couritnes.main {
                try {
                    val response = repositary.getVaccineTestRef(privateKey)
                    if (response != null) {
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
                                        }

                                        val jsonSpecimenCollection = jsonSpecimen.getJSONObject("collection")
                                        val jsonSpecimenCollectionCollector = jsonSpecimenCollection.getJSONObject("collector")

                                        if (jsonSpecimenCollectionCollector.has("name")) {
                                            takenBy = jsonSpecimenCollectionCollector.getString("name")
                                        }


                                    }

                                    if (jsonArrayBody.has("observation")) {
                                        val jsonObservation = jsonArrayBody.getJSONObject("observation")
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
                                        }
                                        if (jsonObservation.has("basedOn")) {
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
                            //listener?.onSuccess(jsonBodayDataObject.getString("reason"))
                        } else {
                            //listener?.onFailure(jsonBodayDataObject.getString("reason"))
                        }
                    }
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                } catch (e: Exception) {
                    listener?.onFailure(e.message!!)
                }
            }
        }
    }

    private fun getTestReportList(privateKey: String):List<TestReport>{
        var tempTestReportList = repositary.getTestReportList(privateKey)
        if (tempTestReportList.isNotEmpty()){
            _testReportList.value = tempTestReportList
        }
        return tempTestReportList
    }

    fun loadData() {
        val userData = repositary.getUserData()

        if (userData != null) {
            fullName.value = userData.fullName
            passportNumber.value = userData.passportNumber
            country.value = userData.countryCode
        }

        val dashBoard = Dashboard()
        dashBoard.fullName = userData.fullName
        dashBoard.passportNo = userData.passportNumber
        dashBoard.idNo = userData.patientIdNo
        dashBoard.privateKey = userData.privateKey
        dashBoard.nationality = userData.nationality
        dashBoard.data = vaccineList.value
        //dashBoard.dataTest = testReportList.value
        if (!userData.privateKey.isNullOrEmpty()) {
            dashBoard.dataTest = getTestReportList(userData.privateKey!!)
        }
        dashBoard.relationShip = Passparams.PARENT
        dashBoard.subId = userData.parentSubscriberId
        dashBoard.dob = userData.dob
        dashBoard.profileImg = userData.profileImage
        _listDashboard.value = listOf(dashBoard)

        val dependentList = repositary.getDependentList()
        if (!dependentList.isNullOrEmpty()) {
            for (dependent in dependentList!!) {
                val dashboard1 = Dashboard()
                dashboard1.fullName = dependent.firstName
                dashboard1.passportNo = dependent.passportNo
                dashboard1.idNo = dependent.idNo
                dashboard1.relationShip = dependent.relationship
                dashboard1.nationality = dependent.nationalityCountry
                //dashboard1.data = vaccineList.value
                dashboard1.privateKey = dependent.privateKey
                //dashboard1.dataTest = testReportList.value
                if (!dependent.privateKey.isNullOrEmpty()) {
                    dashboard1.dataTest = getTestReportList(dependent.privateKey!!)
                }
                dashboard1.subId = dependent.subsId
                dashboard1.dob = dependent.dob
                dashboard1.profileImg = dependent.profileImage
                _listDashboard.value = _listDashboard.value!!.plus(dashboard1)
            }
        }
    }

    fun getParentPrivateKeyFromDb():String?{
        return repositary.getParentPrivateKey()
    }

    fun getDependentPrivateKeyFromDb(subId: String):String?{
        return repositary.getDependentPrivateKey(subId)
    }

    private fun decryptServerKey(pk:String,dob:String):String?{
        return EncryptionUtils.decryptBackupKey(pk,dob)
    }

    fun getPatientPrivateKey() {
        //val getPrivateKey = repositary.getParentPrivateKey()
        val getUser = repositary.getUserData()
        var originalPrivateKey:String?=null
        listener?.onStarted()
        if (getUser.privateKey.isNullOrEmpty()){
            Couritnes.main {
                try {
                    val response = repositary.getParentPrivateKeyFromAPI()
                    if (response.StatusCode == 1){

                        if (!response.PrivateKey.isNullOrEmpty()){
                            val tempPK = response.PrivateKey
                            val tempDob = changeDateFormatForPrivateKeyDecrypt(getUser.dob!!)
                            if ((!decryptServerKey(tempPK,tempDob!!).isNullOrEmpty()) and (!decryptServerKey(tempPK,tempDob!!).equals("invalid",true))){
                                originalPrivateKey = decryptServerKey(tempPK,tempDob)
                            }else{
                                originalPrivateKey = response.PrivateKey
                                listener?.onFailure("Please Check ! API return Original Private Key Instead of Encrypt Private Key")
                            }
                        }else{
                            listener?.onFailure("PrivateKey return Empty")
                            return@main
                        }
                        getUser.privateKey = originalPrivateKey
                        repositary.updateUser(getUser)
                        repositary.savePrivateKey(originalPrivateKey!!)
                        _privateKey.value = originalPrivateKey
                        updatePrivateKeyStatus(getUser.parentSubscriberId!!)
                        listener?.onSuccess("$originalPrivateKey|${getUser.fullName}|${getUser.dob}")
                    }else{
                        listener?.onFailure(response.Message)
                    }

                }catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            if (getUser != null){
                _privateKey.value = getUser.privateKey
                listener?.onSuccess("${getUser.privateKey}|${getUser.fullName}|${getUser.dob}")
            }

        }
    }

    fun getDependentPrivateKey(subId:String) {
        //val getPrivateKey = repositary.getDependentPrivateKey(subId)
        val getUser = repositary.getDependentData(subId)
        var originalPrivateKey:String?=null
        listener?.onStarted()
        if (getUser.privateKey.isNullOrEmpty()){
            Couritnes.main {
                try {
                    val response = repositary.getDependentPrivateKeyFromAPI(subId)
                    if (response.StatusCode == 1){

                        if (!response.PrivateKey.isNullOrEmpty()){
                            val tempPK = response.PrivateKey
                            val tempDob = changeDateFormatForPrivateKeyDecrypt(getUser.dob!!)
                            if ((!decryptServerKey(tempPK,tempDob!!).isNullOrEmpty()) and (!decryptServerKey(tempPK,tempDob!!).equals("invalid",true))){
                                originalPrivateKey = decryptServerKey(tempPK,tempDob)
                            }else{
                                originalPrivateKey = response.PrivateKey
                                listener?.onFailure("Please Check ! API return Original Private Key Instead of Encrypt Private Key")
                            }
                        }
                        getUser.privateKey = originalPrivateKey
                        repositary.updateDependent(getUser)
                        _privateKey.value = originalPrivateKey
                        updatePrivateKeyStatus(getUser.subsId!!)
                        listener?.onSuccess("$originalPrivateKey|${getUser.firstName}|${getUser.dob}")
                    }else{
                        listener?.onFailure(response.Message)
                    }

                }catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            _privateKey.value = getUser.privateKey
            listener?.onSuccess("${getUser.privateKey}|${getUser.firstName}|${getUser.dob}")
        }
    }

    private fun updatePrivateKeyStatus(subId: String){
        Couritnes.main {
            try {
                val response = repositary.updatePrivateKeyStatus(subId,"Y")
                if (response.StatusCode != 1){
                    listener?.onFailure(response.Message)
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

}