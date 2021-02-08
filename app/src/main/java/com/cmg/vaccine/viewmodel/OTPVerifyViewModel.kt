package com.cmg.vaccine.viewmodel

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
import java.lang.Exception
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class OTPVerifyViewModel(
        private val repositary: OTPVerifyRepositary
):ViewModel() {

    var pinTxt:MutableLiveData<String> = MutableLiveData()
    var listener:SimpleListener?=null

    val isExistUser:MutableLiveData<Boolean> = MutableLiveData()

    fun onClick(){
        listener?.onStarted()
        Couritnes.main {
            if (!pinTxt.value.isNullOrEmpty()) {
                try {
                    val response = repositary.OTPVerify(repositary.getPrivateKey()!!,pinTxt.value!!)
                    if (response.success){
                        if (!isExistUser.value!!) {
                            val userData = repositary.getUserData( "N")
                            if (userData != null) {
                                userData.virifyStatus = "Y"
                                repositary.updateVerifyStatus(userData)
                            }
                        }else{
                            var alreadyStored = repositary.getUserData()
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
                }catch (e:NoInternetException){
                    listener?.onFailure(e.message!!)
                }catch (e:SocketTimeoutException){
                    listener?.onFailure(e.message!!)
                }
            }else{
                listener?.onFailure("Please enter your OTP")
            }
        }

    }
}