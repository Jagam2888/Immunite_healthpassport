package com.cmg.vaccine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.request.WebCheckInReq
import com.cmg.vaccine.model.request.WebCheckInReqData
import com.cmg.vaccine.repositary.AirasiaRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.changeDateFormatOnlyDateReverse
import com.google.android.gms.common.api.ApiException
import io.paperdb.Paper
import java.lang.Exception

class AirasiaViewModel(
    private val repositary: AirasiaRepositary
):ViewModel() {

    var listener:SimpleListener?=null

    var eCodeTxt:MutableLiveData<String> = MutableLiveData()
    fun webCheckInAPI(){
        listener?.onStarted()
        Couritnes.main {
            try {
                val userData = Paper.book().read<Dashboard>(Passparams.CURRENT_USER_SUBSID,null)
                var passengerIdno = ""
                var passengerIdType = ""
                var passengerPassportNo = ""
                var passengerPassportExpiryDate=""
                var userDob=""
                var dobEcode=""

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

                if (!userData.dob.isNullOrEmpty()){
                    userDob = changeDateFormatOnlyDateReverse(userData.dob!!)!!
                }

                if ((!eCodeTxt.value.isNullOrEmpty()) and (eCodeTxt.value?.length ==6)) {

                    dobEcode = userDob+eCodeTxt.value

                    val webCheckInReq = WebCheckInReq()
                    val webCheckInReqData = WebCheckInReqData(
                        dobEcode,
                        userDob,
                        passengerIdno,
                        passengerIdType,
                        userData.fullName!!,
                        passengerPassportNo,
                        passengerPassportExpiryDate,
                        userData.privateKey!!,
                        userData.subId!!
                    )
                    webCheckInReq.data = webCheckInReqData
                    val response = repositary.webCheckInApi(webCheckInReq)
                    if (response.StatusCode == 1) {
                        listener?.onSuccess(response.Message)
                    } else {
                        listener?.onFailure("2" + response.Message)
                    }
                }else{
                    listener?.onFailure("Invalid eCode")
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