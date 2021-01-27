package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.repositary.ViewPrivateKeyRepositary

class ViewPrivateKeyViewModel(
    private val repositary: ViewPrivateKeyRepositary
):ViewModel() {

    val _privateKey:MutableLiveData<String> = MutableLiveData()

    val privateKey:LiveData<String>
        get() = _privateKey

    fun getPrivateKey(){
        val email = repositary.getEmail()

        val key = repositary.getPrivateKey(email!!)

        _privateKey.value = key
    }
}