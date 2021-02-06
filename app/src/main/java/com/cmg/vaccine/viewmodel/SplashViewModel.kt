package com.cmg.vaccine.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.LoginActivity
import com.cmg.vaccine.database.LoginPin
import com.cmg.vaccine.database.User
import com.cmg.vaccine.repositary.SplashRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import io.paperdb.Paper
import kotlinx.coroutines.delay
import java.lang.StringBuilder

class SplashViewModel(
    private val repositary: SplashRepositary
) : ViewModel() {
    val data:MutableLiveData<SplashState> = MutableLiveData()

    val liveData : LiveData<SplashState>
        get() = data

    val _loginPin:MutableLiveData<LoginPin> = MutableLiveData()
    val _userData:MutableLiveData<User> = MutableLiveData()
    val _email:MutableLiveData<String> = MutableLiveData()

    val loginPin:LiveData<LoginPin>
    get() = _loginPin

    val userData:LiveData<User>
    get() = _userData

    val email:LiveData<String>
    get() = _email

    init {
        _email.value = repositary.getEmail()
        if (!repositary.getEmail().isNullOrEmpty()) {
            val pinData = repositary.getLoginPin()
            _loginPin.value = pinData

            val user = repositary.getUserData(repositary.getEmail()!!,"Y")
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