package com.cmg.vaccine.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.LoginActivity
import com.cmg.vaccine.repositary.SplashRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import io.paperdb.Paper
import kotlinx.coroutines.delay
import java.lang.StringBuilder

class SplashViewModel() : ViewModel() {
    val data:MutableLiveData<SplashState> = MutableLiveData()

    val liveData : LiveData<SplashState>
        get() = data

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