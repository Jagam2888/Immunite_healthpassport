package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.User
import com.cmg.vaccine.repositary.SplashRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import kotlinx.coroutines.delay

class SplashViewModel(
    private val repositary: SplashRepositary
) : ViewModel() {
    val data:MutableLiveData<SplashState> = MutableLiveData()

    val liveData : LiveData<SplashState>
        get() = data

    val _loginPin:MutableLiveData<LoginPin> = MutableLiveData()
    val _userData:MutableLiveData<User> = MutableLiveData()
    val _subId:MutableLiveData<String> = MutableLiveData()

    val loginPin:LiveData<LoginPin>
    get() = _loginPin

    val userData:LiveData<User>
    get() = _userData

    val subId:LiveData<String>
    get() = _subId

    init {
        _subId.value = repositary.getSubId()
        if (!repositary.getSubId().isNullOrEmpty()) {
            val pinData = repositary.getLoginPin()
            _loginPin.value = pinData

            val user = repositary.getUserData(repositary.getSubId()!!)
            _userData.value = user
        }
    }

    fun initSplashScreen(){
        Couritnes.main {
            try {
                delay(2000)
                data.postValue(SplashState.MainActivity())
            }catch (e: NoInternetException){
                e.printStackTrace()
            }
        }
    }

    sealed class SplashState {
        class MainActivity : SplashState()
    }
}