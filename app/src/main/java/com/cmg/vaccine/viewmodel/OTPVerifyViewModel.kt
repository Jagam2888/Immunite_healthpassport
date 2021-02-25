package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.OTPVerifyRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class OTPVerifyViewModel(
        private val repositary: OTPVerifyRepositary
):ViewModel() {

    var pinTxt:MutableLiveData<String> = MutableLiveData()
    var listener:SimpleListener?=null

    val isExistUser:MutableLiveData<Boolean> = MutableLiveData()

    var _txtOTP:MutableLiveData<String> = MutableLiveData()
    val txtOTP:LiveData<String>
    get() = _txtOTP

    var userSubId:MutableLiveData<String> = MutableLiveData()

    var displaymobileNumber = ObservableField<String>()

    init {
        val userData = repositary.getUserData()
        if (userData != null){
            if (!userData.mobileNumber.isNullOrEmpty()) {
                var mobileNumberTest = userData.mobileNumber
                if (mobileNumberTest.length > 8) {
                    val myName = StringBuilder(mobileNumberTest)
                    myName.setCharAt(2, 'x')
                    myName.setCharAt(3, 'x')
                    myName.setCharAt(4, 'x')
                    myName.setCharAt(5, 'x')
                    mobileNumberTest = myName.toString()
                }
                displaymobileNumber.set("+" + userData.countryCode + mobileNumberTest)
                Log.d("mobilenumber", displaymobileNumber.get()!!)
            }
        }
    }


    fun onResendTac(){
        listener?.onStarted()
        Couritnes.main {
            try {
                val response = repositary.resendOTP(userSubId.value!!)
                listener?.onFailure(response.Message)
                /*if (response.success){
                    listener?.onSuccess(response.message)
                }else{
                    listener?.onSuccess(response.message)
                }*/
            }catch (e: APIException) {
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }
        }
    }

    fun onClick(){
        listener?.onStarted()
        Couritnes.main {
            if (!pinTxt.value.isNullOrEmpty()) {
                try {
                    val response = repositary.OTPVerify(userSubId.value!!, pinTxt.value!!)
                    if (response.success){
                        if (!isExistUser.value!!) {
                            val userData = repositary.getUserData()
                            if (userData != null) {
                                userData.virifyStatus = "Y"
                                repositary.updateVerifyStatus(userData)
                            }
                        }else{
                            var alreadyStored = repositary.getUserDataPref()
                            val gson = Gson()
                            val type: Type = object : TypeToken<User>() {}.type
                            var userData = gson.fromJson<User>(alreadyStored, type)
                            repositary.updateUser(userData)

                        }
                        listener?.onSuccess(response.message)
                    }else{
                        listener?.onFailure(response.message)
                    }
                    /*if (pinTxt.value == "123456") {
                        if (!isExistUser.value!!) {
                            val userData = repositary.getUserData(repositary.getEmail()!!, "N")
                            if (userData != null) {
                                userData.virifyStatus = "Y"
                                repositary.updateVerifyStatus(userData)

                                listener?.onSuccess("Verification Successful")

                            }
                        }else{
                            var alreadyStored = repositary.getUserData()
                            val gson = Gson()
                            val type: Type = object : TypeToken<User>() {}.type
                            var userData = gson.fromJson<User>(alreadyStored, type)

                            repositary.updateUser(userData)
                            listener?.onSuccess("Update Successful")
                        }
                    }else{
                        listener?.onFailure("OTP is Wrong")
                    }*/
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                }catch (e: NoInternetException){
                    listener?.onFailure(e.message!!)
                }catch (e: SocketTimeoutException){
                    listener?.onFailure(e.message!!)
                }
            }else{
                listener?.onFailure("Please enter your OTP")
            }
        }

    }
}