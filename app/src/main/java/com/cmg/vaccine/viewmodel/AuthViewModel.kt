package com.cmg.vaccine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.AuthRepositary
import com.cmg.vaccine.util.Couritnes
import java.net.SocketTimeoutException

class AuthViewModel(
    private val repositary: AuthRepositary
) : ViewModel() {

    var email:MutableLiveData<String> = MutableLiveData()
    var password:MutableLiveData<String> = MutableLiveData()
    //var password:String?=null

    var authListener:SimpleListener?=null

    init {
        val emailTemp = repositary.getEmail()
        if (!emailTemp.isNullOrEmpty())
            email.value = emailTemp
    }

    fun onLoginClick(){
        /*authListener?.onStarted()
        if (!email.value.isNullOrEmpty() and !password.value.isNullOrEmpty()){
            val user = repositary.getLogin(email.value!!,password.value!!)
            if(user != null){
                repositary.saveUserEmail(email.value!!)
                authListener?.onSuccess(user.email)
            }else{
                authListener?.onFailure("Login Failed")
            }
        }else{
            authListener?.onFailure("Field(s) Missing")
        }*/

    }


    /*fun onLoginClick(view:View){
        authListener?.onStarted()
        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            authListener?.onFailure("Field(s) Missing")
            return
        }

        Couritnes.main {
            try {
                val authReq = AuthRequest()
                authReq.username = username
                authReq.password = password
                val authResponse = repositary.loginUser(authReq)
                authResponse.token?.let {
                    authListener?.onSuccess(authResponse)
                    return@main
                }
                //authListener?.onFailure(authResponse.)

            }catch (e:APIException){
                authListener?.onFailure(e.message.toString())
            }catch (e:NoInternetException){
                authListener?.onFailure(e.message.toString())
            }catch (e:SocketTimeoutException){
                authListener?.onFailure(e.toString())
            }
        }
    }*/

}