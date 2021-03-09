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
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.changeDateFormatForPrivateKeyDecrypt
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
    }

    fun disableLoginPin(){
        val loginPin = repositary.getLoginPin()
        if (loginPin != null) {
            loginPin.enable = "N"
            repositary.updateLoginPin(loginPin)
        }

        val user = repositary.getUserData()
        if (user != null){
            dob.set(changeDateFormatForPrivateKeyDecrypt(user.dob!!))
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
                val response = repositary.getVaccineListBlockChain(repositary.getPrivateKey()!!)
                if (!response.data.data.isNullOrEmpty()){
                    response.data.data.forEach { vaccineData->
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
                val response = repositary.getTestReportList(repositary.getPrivateKey()!!)
                if (!response.data.data.isNullOrEmpty()){
                    response.data.data.forEach { report->
                        val testReport = TestReport(
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