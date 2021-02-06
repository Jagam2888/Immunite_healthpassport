package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.LoginPinRepositary
import com.cmg.vaccine.util.Couritnes
import java.lang.Exception

class LoginPinViewModel(
        private val repositary: LoginPinRepositary
):ViewModel() {

    var pin:MutableLiveData<String> = MutableLiveData()
    var tempPin:MutableLiveData<String> = MutableLiveData()
    val _getPin:MutableLiveData<LoginPin> = MutableLiveData()
    var labelTxt:MutableLiveData<String> = MutableLiveData()
    var isCreate:MutableLiveData<Boolean> = MutableLiveData()
    val getPin:LiveData<LoginPin>
    get() = _getPin

    var listener:SimpleListener?=null
    var isDoneReEnter:Boolean = false

    init {
        labelTxt.value = "Enter Your 4 Digits PIN"
        _getPin.value = repositary.getLoginPin()
    }

    fun onLogin(){
        if (!pin.value.isNullOrEmpty()) {
            //Couritnes.main {
                try {
                    if (isDoneReEnter) {
                        if (tempPin.value == pin.value) {
                            val loginPin = repositary.getLoginPin()

                            if (loginPin == null) {
                                val insertPin = LoginPin(
                                        pin.value!!,
                                        "Y"
                                )
                                repositary.insertPin(insertPin)
                                listener?.onSuccess("PIN Created SuccessFully")
                            } else {
                                loginPin.pin = pin.value!!
                                repositary.updateLoginPin(loginPin)
                            }
                        }else{
                            isDoneReEnter = false
                            listener?.onFailure("Please enter correct PIN")
                        }
                    }else{
                        labelTxt.value = "Re-enter Your 4 Digits PIN"
                        tempPin.value = pin.value
                        pin.value = ""
                        isDoneReEnter = true
                    }
                }catch (e:Exception){
                    listener?.onFailure(e.message!!)
                }

            //}
        }

    }
}