package com.cmg.vaccine.listener

import com.cmg.vaccine.model.response.AuthResponse

interface AuthListener {
    fun onStarted()
    fun onSuccess(authResponse: AuthResponse)
    fun onFailure(msg:String)
}