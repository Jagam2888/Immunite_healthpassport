package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.SignUpReqData
import com.cmg.vaccine.repositary.SignUpRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.isValidEmail

class SignupViewModel(
    private val signUpRepositary: SignUpRepositary
):ViewModel() {
    var email1:String?=null
    var email2:String?=null
    var firstName:String?=null
    var lastName:String?=null
    var password:String?=null
    var reTypePassword:String?=null

    var idType:String?=null
    var idNumber:String?=null
    val selectedItemIDType = ObservableInt()

    var listener:SimpleListener?=null


    fun onSignUp(){

        if(!firstName.isNullOrEmpty() and !lastName.isNullOrEmpty() and !email1.isNullOrEmpty() and !email2.isNullOrEmpty() and !password.isNullOrEmpty()) {
            if (password.equals(reTypePassword)) {
                if (isValidEmail(email1!!)) {
                    if (isValidEmail(email2!!)) {
                        if (selectedItemIDType.get() == 0) {
                            idType = "MyKad"
                        } else {
                            idType = "Passport"
                        }

                        /*var user = User(
                        "", 1, 123456, email1!!, email2!!, firstName!!, "M", lastName!!,
                        "123455", idNumber!!, idType!!, "", 1, 123444, 12!!,
                    )*/
                        var user = User(
                            "",
                            1,
                            System.currentTimeMillis(),
                            email1!!,
                            email2!!,
                            firstName!!,
                            "M",
                            lastName!!,
                            "123455",
                            idNumber!!,
                            idType!!,
                            1,
                            "",
                            password!!,
                            1,
                            System.currentTimeMillis()
                        )

                        signUpRepositary.saveUser(user)
                        listener?.onSuccess("success")

                    } else {
                        listener?.onFailure("Your Email Address 2 is Invalid")
                    }
                } else {
                    listener?.onFailure("Your Email Address 1 is Invalid")
                }
            } else {
                listener?.onFailure("Password Mismatch")
            }
        }else{
            listener?.onFailure("All Field(s) Mandatory")
        }

    }
}