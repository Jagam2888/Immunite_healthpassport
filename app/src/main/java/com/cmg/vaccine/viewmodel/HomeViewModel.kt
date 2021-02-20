package com.cmg.vaccine.viewmodel

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
import com.cmg.vaccine.model.response.HomeResponse
import com.cmg.vaccine.model.response.TestReportListResponseData
import com.cmg.vaccine.model.response.VaccineListResponseData
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
    val passportNumber:MutableLiveData<String> = MutableLiveData()
    val country:MutableLiveData<String> = MutableLiveData()
    val _privateKey:MutableLiveData<String> = MutableLiveData()

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


    fun setCurrentItem(position:Int){
        _currentPagerPosition.value = position
    }

    fun setUser(){
        var listuser:List<SwitchProfile>?=null
        val parent = repositary.getUserData()
        var user = SwitchProfile()

        user.fullName = parent.fullName
        user.tyep = "Principal"
        listuser = listOf(user)

        val dependent = repositary.getDependentList()

        if (!dependent.isNullOrEmpty()) {
            for (child in dependent!!) {
                var getChild = SwitchProfile()
                getChild.fullName = child.firstName
                getChild.tyep = child.relationship
                listuser = listuser?.plus(getChild)
            }
        }

        _users.value = listuser
    }

    fun loadVaccineList(){
        var vaccineList = repositary.getVaccineList()

        if (vaccineList.isNullOrEmpty()) {
            listener?.onStarted()
            Couritnes.main {
                try {

                    val response = repositary.getVaccineList(repositary.getSubsId()!!)
                    //val response = repositary.getVaccineList("60135720210213180206476394")
                    if (!response.data.isNullOrEmpty()) {

                        response.data.forEach { vaccineListResponseData ->
                            val vaccine = Vaccine(
                                    vaccineListResponseData.brandName,
                                    vaccineListResponseData.facilityName,
                                    vaccineListResponseData.gitn,
                                    vaccineListResponseData.gsicodeSerialCode,
                                    vaccineListResponseData.itemBatch,
                                    vaccineListResponseData.item_expiry,
                                    vaccineListResponseData.malNo,
                                    vaccineListResponseData.manufacturerName,
                                    vaccineListResponseData.manufacturerNo,
                                    vaccineListResponseData.nfcTag,
                                    vaccineListResponseData.patientSeqNo,
                                    vaccineListResponseData.privateKey,
                                    vaccineListResponseData.uuidTagNo,
                                    vaccineListResponseData.vaccinationStatus,
                                    vaccineListResponseData.vaccineDate,
                                    vaccineListResponseData.vaccineType,
                                    vaccineListResponseData.vccprivatekey
                            )
                            repositary.insertVaccine(vaccine)
                        }
                    }
                    vaccineList = repositary.getVaccineList()
                    _vaccineList.value = vaccineList
                    //loadTestReportList()
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            _vaccineList.value = vaccineList
        }
    }

    fun loadTestReportList(){
        var testReportList = repositary.getTestReportList()
        if (testReportList.isNullOrEmpty()) {
            Couritnes.main {
                try {
                    val response = repositary.getTestReportList(repositary.getSubsId()!!)
                    //val response = repositary.getTestReportList("60135720210213180206476394")
                    if (!response.data.isNullOrEmpty()) {
                        response.data.forEach { testReportListResponseData ->
                            val testReport = TestReport(
                                    testReportListResponseData.filePath,
                                    testReportListResponseData.observationCodeSnomedCt,
                                    testReportListResponseData.observationDateTime,
                                    testReportListResponseData.observationResult,
                                    testReportListResponseData.performerAddTxt,
                                    testReportListResponseData.performerAddType,
                                    testReportListResponseData.performerAddUse,
                                    testReportListResponseData.performerContactTelecom,
                                    testReportListResponseData.performerContactTelecomValue,
                                    testReportListResponseData.performerName,
                                    testReportListResponseData.performerQualificationIdentifier,
                                    testReportListResponseData.performerQualificationIssuerName,
                                    testReportListResponseData.performerType,
                                    testReportListResponseData.specimenCode,
                                    testReportListResponseData.specimenDateSampleCollected,
                                    testReportListResponseData.specimenName,
                                    testReportListResponseData.status,
                                    testReportListResponseData.subsId,
                                    testReportListResponseData.testCode,
                                    testReportListResponseData.testSeqno,
                            )
                            repositary.insertTestReport(testReport)
                        }
                    }
                    testReportList = repositary.getTestReportList()
                    _testReportList.value = testReportList
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            _testReportList.value = testReportList
        }
        setUser()
        listener?.onSuccess("success")
    }

    fun loadData() {
        val userData = repositary.getUserData()
/*

        val dashboardData = DashboardVaccineData()

        dashboardData.vaccineName = "Covid-19 Vaccine (Pfizer Dose 3)"
        dashboardData.vaccineDate = "11/2/2021"
        dashboardData.brandname = "Seeram"
        dashboardData.manufacturer = "Pfizer Pvt Ltd"
        dashboardData.clinic = "Americal Public Hospital"

        val dashboardData1 = DashboardVaccineData()

        dashboardData1.vaccineName = "Covid-19 Vaccine (Pfizer Dose 2)"
        dashboardData1.vaccineDate = "11/2/2021"
        dashboardData1.brandname = "Seeram"
        dashboardData1.manufacturer = "Pfizer Pvt Ltd"
        dashboardData1.clinic = "Americal Public Hospital"

        val dashboardData2 = DashboardVaccineData()

        dashboardData2.vaccineName = "Covid-19 Vaccine (Pfizer Dose 1)"
        dashboardData2.vaccineDate = "11/2/2021"
        dashboardData2.brandname = "Seeram"
        dashboardData2.manufacturer = "Pfizer Pvt Ltd"
        dashboardData2.clinic = "Americal Public Hospital"

        val dashboardTestData = DashboardTestData()
        dashboardTestData.vaccineName = "Covid-19 (rRT-PCR) test"
        dashboardTestData.vaccineDate = "11/2/2021"
        dashboardTestData.specimenDate = "23/5/2020"
        dashboardTestData.takenBy = "Malaysia Lab"
        dashboardTestData.testBy = "Malaysia Lab"
        dashboardTestData.result = "Negative"

        val dashboardTestData1 = DashboardTestData()
        dashboardTestData1.vaccineName = "Covid-19 (rRT-PCR) test"
        dashboardTestData1.vaccineDate = "11/2/2021"
        dashboardTestData1.specimenDate = "23/5/2020"
        dashboardTestData1.takenBy = "Malaysia Lab"
        dashboardTestData1.testBy = "Malaysia Lab"
        dashboardTestData1.result = "Positive"

        val dashboardTestData2 = DashboardTestData()
        dashboardTestData2.vaccineName = "Covid-19 (rRT-PCR) test"
        dashboardTestData2.vaccineDate = "11/2/2021"
        dashboardTestData2.specimenDate = "23/5/2020"
        dashboardTestData2.takenBy = "Malaysia Lab"
        dashboardTestData2.testBy = "Malaysia Lab"
        dashboardTestData2.result = "Negative"

        listDashboardData = listOf(dashboardData,dashboardData1,dashboardData2)
        listDashboardTestData = listOf(dashboardTestData,dashboardTestData1,dashboardTestData2)*/

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
        dashBoard.data = vaccineList.value
        dashBoard.dataTest = testReportList.value
        dashBoard.relationShip = "Principal"
        _listDashboard.value = listOf(dashBoard)

        val dependentList = repositary.getDependentList()
        if (!dependentList.isNullOrEmpty()) {
            for (dependent in dependentList!!) {
                val dashboard1 = Dashboard()
                dashboard1.fullName = dependent.firstName
                dashboard1.passportNo = dependent.passportNo
                dashboard1.idNo = dependent.idNo
                dashboard1.relationShip = dependent.relationship
                dashboard1.data = vaccineList.value
                dashboard1.privateKey = dependent.privateKey
                dashboard1.dataTest = testReportList.value
                _listDashboard.value = _listDashboard.value!!.plus(dashboard1)
            }
        }


    }

    /*fun loadVaccineDetail(){
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
    }*/
}