package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.SettingsRepositary

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

        loginPin.enable = "N"
        repositary.updateLoginPin(loginPin)
    }

    fun enableLoginPin(){
        val loginPin = repositary.getLoginPin()

        loginPin.enable = "Y"
        repositary.updateLoginPin(loginPin)
    }

}