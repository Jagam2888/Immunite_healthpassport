package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.User
import com.cmg.vaccine.repositary.RestoreBackupOptionListRepositary

class RestoreBackupOptionListViewModel(
        private val repositary: RestoreBackupOptionListRepositary
):ViewModel() {

    var _userData:MutableLiveData<User> = MutableLiveData()

    val userData:LiveData<User>
    get() = _userData

    var _loginPin:MutableLiveData<LoginPin> = MutableLiveData()

    val loginPin:LiveData<LoginPin>
    get() = _loginPin

    fun getUser(){
        val user = repositary.getUserData()
        if (user != null){
            _userData.value = user
            repositary.saveSubId(user.parentSubscriberId!!)
        }

        val getLogin = repositary.getLogin()
        if (getLogin != null){
            _loginPin.value = getLogin
        }
    }
}