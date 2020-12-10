package com.cmg.vaccine.viewmodel

import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.AuthListener
import com.cmg.vaccine.model.request.AuthRequest
import com.cmg.vaccine.repositary.AuthRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import io.paperdb.Paper
import java.lang.StringBuilder
import java.net.SocketException
import java.net.SocketTimeoutException

class AuthViewModel(
    private val repositary: AuthRepositary
) : ViewModel() {

    var username:String?=null
    var password:String?=null

    var authListener:AuthListener?=null

    fun onLoginClick(view:View){
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
    }

}