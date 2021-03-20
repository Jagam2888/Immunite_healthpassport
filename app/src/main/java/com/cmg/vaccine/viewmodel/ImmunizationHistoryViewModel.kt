package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.ImmunizationHistoryReq
import com.cmg.vaccine.model.request.ImmunizationHistoryReqData
import com.cmg.vaccine.repositary.ImmunizationHistoryRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.changeDateFormatISO8601
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.SocketTimeoutException


class ImmunizationHistoryViewModel(
        private val repositary: ImmunizationHistoryRepositary
):ViewModel() {

    var specimen:MutableLiveData<String> = MutableLiveData()
    var specimenName:MutableLiveData<String> = MutableLiveData()
    var manufactureBrand:MutableLiveData<String> = MutableLiveData()
    var dateSample:MutableLiveData<String> = MutableLiveData()
    var timeSample:MutableLiveData<String> = MutableLiveData()
    var testCode:MutableLiveData<String> = MutableLiveData()
    var observation:MutableLiveData<String> = MutableLiveData()
    var statusFinal:MutableLiveData<String> = MutableLiveData()
    var medicalOfficer:MutableLiveData<String> = MutableLiveData()
    var qualification:MutableLiveData<String> = MutableLiveData()
    var qualificationIssuerName:MutableLiveData<String> = MutableLiveData()
    var type:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var contactCode:MutableLiveData<String> = MutableLiveData()
    var address1:MutableLiveData<String> = MutableLiveData()
    var address2:MutableLiveData<String> = MutableLiveData()
    var city:MutableLiveData<String> = MutableLiveData()
    var state:MutableLiveData<String> = MutableLiveData()
    var country:MutableLiveData<String> = MutableLiveData()
    var zipCode:MutableLiveData<String> = MutableLiveData()
    var selectedItemContactCode = ObservableField<String>()

    var filePath:MutableLiveData<String> = MutableLiveData()
    var fileName:MutableLiveData<String> = MutableLiveData()

    var listener:SimpleListener?=null

    var userSubId:MutableLiveData<String> = MutableLiveData()

    fun onSubmitClick(){
        listener?.onStarted()
        //remove first char if zero
        if (!contactNumber.value.isNullOrEmpty()){
            if (contactNumber.value!!.startsWith("0") and selectedItemContactCode.get().equals("60")){
                contactNumber.value = contactNumber.value!!.drop(1)
            }
        }

        val immunizationHistoryReq = ImmunizationHistoryReq()
        var immunizationHistoryReqData = ImmunizationHistoryReqData()

        immunizationHistoryReqData.specimenCode = specimen.value
        immunizationHistoryReqData.specimenName = specimenName.value
        immunizationHistoryReqData.manufacturerBrand = manufactureBrand.value
        immunizationHistoryReqData.specimenSampleCollected = changeDateFormatISO8601("${dateSample.value} ${timeSample.value}:00")
        immunizationHistoryReqData.testCode = testCode.value
        immunizationHistoryReqData.observationCode = observation.value
        immunizationHistoryReqData.observationDate = changeDateFormatISO8601("${dateSample.value} ${timeSample.value}:00")
        immunizationHistoryReqData.performerName = medicalOfficer.value
        immunizationHistoryReqData.performerQualiIdentifier = qualification.value
        immunizationHistoryReqData.performerQualiIssuer = qualificationIssuerName.value
        immunizationHistoryReqData.performerType = type.value
        immunizationHistoryReqData.performerAddType = "Work"
        immunizationHistoryReqData.subId = userSubId.value
        immunizationHistoryReqData.performerContactTelecomValue = selectedItemContactCode.get()+contactNumber.value
        immunizationHistoryReqData.performerAddText = address1.value
        immunizationHistoryReqData.status = statusFinal.value
        immunizationHistoryReqData.city = city.value
        immunizationHistoryReqData.state = state.value
        immunizationHistoryReqData.country = state.value
        immunizationHistoryReqData.postcode = zipCode.value

        immunizationHistoryReq.data = immunizationHistoryReqData

        Couritnes.main {
            try {
                // MultipartBody.Part is used to send also the actual file name

                // MultipartBody.Part is used to send also the actual file name
                val file = File(filePath.value)
                var filename = file.name
                val filenameArray = filename.split(".")
                if (!filename.isNullOrEmpty()) {
                    if (filenameArray?.size!! > 1) {
                        filename = filenameArray?.get(0) + "_" + System.currentTimeMillis() + ".pdf"
                    }
                }
                val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val body = MultipartBody.Part.createFormData("file", filename, requestFile)
                val response = repositary.immunizationHistory(body, immunizationHistoryReq)
                Log.d("response",response.Message)
                listener?.onSuccess(response.Message)
            }catch (e: APIException){
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }
        }
        
    }
}