package com.cmg.vaccine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.ChangePasswordRepositary
import com.cmg.vaccine.util.Couritnes

import java.lang.Exception

class ChangePasswordViewModel(
        private val repositary: ChangePasswordRepositary
):ViewModel() {

    var currentPassword:MutableLiveData<String> = MutableLiveData()
    var password:MutableLiveData<String> = MutableLiveData()
    var reTypePassword:MutableLiveData<String> = MutableLiveData()

    var listener: SimpleListener?=null

    fun onClick(){
        /*Couritnes.main {
            try {
                val user = repositary.getUser()
                if (!currentPassword.value.isNullOrEmpty() and !password.value.isNullOrEmpty()) {
                    if (password.value.equals(reTypePassword.value)) {
                        if (user.password.equals(currentPassword.value)) {
                            user.password = password.value!!
                            //val response = repositary.changePassword(password.value!!)
                            val response = repositary.updatePassword(user)
                            if (response > 0){
                                listener?.onSuccess("Password Changed Successfully")
                            }else{
                                listener?.onFailure("Password Changed Failed")
                            }
                        } else {
                            listener?.onFailure("Your Current Password is Wrong")
                        }
                    } else {
                        listener?.onFailure("Your New Password and Re-Type Password Mismatch")
                    }
                }else{
                    listener?.onFailure("Please Fill All Field(s)")
                }
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }*/

    }
}