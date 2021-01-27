package com.cmg.vaccine.listener

interface SimpleListener {
    fun onStarted()
    fun onSuccess(msg:String)
    fun onFailure(msg: String)
}