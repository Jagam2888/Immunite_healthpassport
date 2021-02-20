package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.SettingsRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import java.net.SocketTimeoutException

class SettingsViewModel(
    private val repositary: SettingsRepositary
):ViewModel() {

    val _loginPinEnable:MutableLiveData<String> = MutableLiveData()
    val loginPinEnable:LiveData<String>
    get() = _loginPinEnable


    var listener:SimpleListener?=null

    init {
        val getLoginPin = repositary.getLoginPin()
        if (getLoginPin != null){
            _loginPinEnable.value = getLoginPin.enable
        }
    }

    fun disableLoginPin(){
        val loginPin = repositary.getLoginPin()
        if (loginPin != null) {
            loginPin.enable = "N"
            repositary.updateLoginPin(loginPin)
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

        getVaccineFromAPI()
    }

    private fun getVaccineFromAPI(){
        Couritnes.main {
            try {
                val response = repositary.getVaccineList(repositary.getSubId()!!)
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
                getTestReportListFromAPI()
            }catch (e: APIException) {
                listener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                listener?.onFailure(e.message!!)
            } catch (e: SocketTimeoutException) {
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun getTestReportListFromAPI(){
        Couritnes.main {
            try {
                val response = repositary.getTestReportList(repositary.getSubId()!!)
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
                listener?.onSuccess("Sync Successfully")
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