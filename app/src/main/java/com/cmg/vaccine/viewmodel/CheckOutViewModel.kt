package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.User
import com.cmg.vaccine.repositary.CheckOutRepositary

class CheckOutViewModel(
    private val repositary: CheckOutRepositary
):ViewModel() {

    val _userData:MutableLiveData<User> = MutableLiveData()
    val userData:LiveData<User>
    get() = _userData

    init {
        val user = repositary.getUser()
        if (user != null){
            _userData.value = user
        }
    }
}