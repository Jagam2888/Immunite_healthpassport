package com.cmg.vaccine.viewmodel

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.R
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.request.SignUpReqData
import com.cmg.vaccine.repositary.TellUsRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class TellUsMoreViewModel(
    private val repositary: TellUsRepositary
):ViewModel() {

    var contact:String?=null
    var countryCode:String?=null
    var gender:Gender = Gender.MALE
    //var isTermsRead:Boolean = false
    var isTermsRead:MutableLiveData<Boolean> = MutableLiveData()
    val selectedItemContactCode = ObservableInt()
    var isChecked = ObservableBoolean()

    var listener:SimpleListener?=null

    fun onRegister(view:View){
        if(!contact.isNullOrEmpty()) {
            if (isChecked.get()) {
                var alreadyStored = repositary.getUserData()
                val gson = Gson()
                val type: Type = object : TypeToken<User>() {}.type
                var userData = gson.fromJson<User>(alreadyStored, type)

                val phoneCode = view.context.resources.getStringArray(R.array.code)
                var code = phoneCode.get(selectedItemContactCode.get())

                userData.countryCode = "MY"
                userData.gender = gender.name
                userData.mobileNumber = code + contact!!

                repositary.saveUserEmail(userData.email)
                userData.patientSeqno = 1

                userData.createdBy = 0
                userData.updatedBy = 0
                userData.createdDate = 123454
                userData.updatedDate = 1234445

                val signUpReq = SignUpReq()
                var signUpReqData = SignUpReqData()

                signUpReqData.patientIdNo = userData.patientIdNo
                signUpReqData.lastName = userData.lastName
                signUpReqData.firstName = userData.firstName
                signUpReqData.email = userData.email
                signUpReqData.countryCode = userData.countryCode
                signUpReqData.gender = userData.gender
                signUpReqData.patientIdType = userData.patientIdType
                signUpReqData.mobileNumber = userData.mobileNumber
                signUpReqData.backupEmail = userData.backupEmail
                /*signUpReqData.createdBy = 1
                signUpReqData.updatedBy = 1
                signUpReqData.createdDate = System.currentTimeMillis()
                signUpReqData.updatedDate = System.currentTimeMillis()*/

                signUpReq.data = signUpReqData

                Couritnes.main {
                    try {
                        val response = repositary.signUp(signUpReq)
                        //if (!response.data.email.isNullOrEmpty()) {
                            userData.privateKey = response.privatekey
                            repositary.insertUser(userData)
                       // }
                        listener?.onSuccess(response.Message)
                    } catch (e: APIException) {
                        listener?.onFailure(e.message!!)
                    } catch (e: NoInternetException) {
                        listener?.onFailure(e.message!!)
                    } catch (e: SocketTimeoutException) {
                        listener?.onFailure(e.message!!)
                    }
                }
            } else {
                listener?.onFailure("Please Read Terms and Condtition")
            }
        }else{
            listener?.onFailure("Mobile Number Missing")
        }
    }
}