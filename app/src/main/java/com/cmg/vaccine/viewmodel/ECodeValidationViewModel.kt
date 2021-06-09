package com.cmg.vaccine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.request.WebCheckInReq
import com.cmg.vaccine.model.request.WebCheckInReqData
import com.cmg.vaccine.repositary.ECodeValidationRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.changeDateFormatOnlyDateReverse
import com.google.android.gms.common.api.ApiException
import io.paperdb.Paper
import java.lang.Exception

class ECodeValidationViewModel(
    private val repositary: ECodeValidationRepositary
):ViewModel() {

    var fullName: MutableLiveData<String> = MutableLiveData()
    var passportNo: MutableLiveData<String> = MutableLiveData()
    var idNo: MutableLiveData<String> = MutableLiveData()

    var dob:MutableLiveData<String> = MutableLiveData()
    var arrivalDestination:MutableLiveData<String> = MutableLiveData()
    var flightNo:MutableLiveData<String> = MutableLiveData()
    var eCode:MutableLiveData<String> = MutableLiveData()

    var listener:SimpleListener?=null
    var userData:Dashboard?=null

    init {
        userData = Paper.book().read<Dashboard>(Passparams.CURRENT_USER_SUBSID,null)
        if (userData != null){
            /*if (!userData!!.fullName.isNullOrEmpty()){
                fullName.value = userData!!.fullName
            }

            if (!userData!!.passportNo.isNullOrEmpty()){
                passportNo.value = userData!!.passportNo
            }*/

            if (!userData!!.idNo.isNullOrEmpty()){
                idNo.value = userData!!.idNo
            }
        }
    }

    fun onClick(){
        if ((!flightNo.value.isNullOrEmpty()) and (!dob.value.isNullOrEmpty())
            and (!fullName.value.isNullOrEmpty())){
            webCheckInAPI()
        }else{
            listener?.onFailure("Please fill all fields")
        }
    }


    private fun webCheckInAPI(){
        Couritnes.main {
            try {
                val dobEcode = changeDateFormatOnlyDateReverse(dob.value!!)!!+eCode.value
                val webCheckInReq = WebCheckInReq()
                val webCheckInReqData = WebCheckInReqData(
                    dobEcode,
                    changeDateFormatOnlyDateReverse(dob.value!!)!!,
                    userData?.idNo!!,
                    userData?.idType!!,
                    userData?.fullName!!,
                    userData?.passportNo!!,
                    userData?.passportExpiry!!,
                    userData?.privateKey!!,
                    userData?.subId!!
                )
                webCheckInReq.data = webCheckInReqData
                val response = repositary.webCheckInApi(webCheckInReq)
                if (response.StatusCode == 1){
                    listener?.onSuccess(response.Message)
                }else{
                    listener?.onFailure("2"+response.Message)
                }
            }catch (e: ApiException){
                listener?.onShowToast("2"+e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure("3"+e.message!!)
            }catch (e: Exception){
                listener?.onShowToast("2"+e.message!!)
            }
        }

    }


}