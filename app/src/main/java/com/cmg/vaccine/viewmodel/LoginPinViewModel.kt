package com.cmg.vaccine.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.R
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
    var title:MutableLiveData<String> = MutableLiveData()
    val _getPin:MutableLiveData<LoginPin> = MutableLiveData()
    var labelTxt:MutableLiveData<String> = MutableLiveData()
    var status = ObservableField<String>()
    val isCreateOrUpdate = ObservableBoolean()
    val getPin:LiveData<LoginPin>
    get() = _getPin

    var listener:SimpleListener?=null
    var isDoneReEnter:Boolean = false





    fun loadValues(context: Context){
        labelTxt.value = "Enter Your 4 Digits PIN"
        _getPin.value = repositary.getLoginPin()
        if (status.get() == ""){
            isCreateOrUpdate.set(false)
            title.value = context.resources.getString(R.string.login)
        }else{
            isCreateOrUpdate.set(true)
            if (status.get() == "create"){
                title.value = context.resources.getString(R.string.create_pin)
            }else{
                title.value = context.resources.getString(R.string.update_pin)
            }
        }
        //isCreateOrUpdate.set(status.get().isNullOrEmpty())
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
                                listener?.onSuccess("PIN Updated SuccessFully")
                            }
                        }else{
                            isDoneReEnter = false
                            labelTxt.value = "Enter Your 4 Digits PIN"
                            tempPin.value = ""
                            pin.value = ""
                            listener?.onFailure("Incorrect PIN")
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