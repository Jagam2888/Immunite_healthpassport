package com.cmg.vaccine.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.LoginActivity
import com.cmg.vaccine.repositary.SplashRepositary
import io.paperdb.Paper
import java.lang.StringBuilder

class SplashViewModel(
    private val splashRepositary: SplashRepositary
) : ViewModel() {
    var ipAddress:String?=null
    var port:String?=null
    var url: MutableLiveData<String>?=null

    internal fun getURL() : MutableLiveData<String>? {
        if(url == null) {
            url = MutableLiveData()
            url?.value = splashRepositary.getURL()
        }
        return url
    }


    fun onSetURLClick(view: View){
        val address = StringBuilder()
        if (ipAddress.isNullOrEmpty())
            return

        address.append("http://")
        address.append(ipAddress)

        if (!port.isNullOrEmpty()){
            address.append(":")
            address.append(port)
        }
        address.append("/api/")
        //Paper.book().write("url",address.toString())
        splashRepositary.addURL(address.toString())

        Intent(view.context,LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            view.context.startActivity(it)
        }

    }
}