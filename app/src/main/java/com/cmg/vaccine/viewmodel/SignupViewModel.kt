package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.data.Gender
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.SignUpRepositary
import com.cmg.vaccine.util.isValidEmail

class SignupViewModel(
    private val signUpRepositary: SignUpRepositary
):ViewModel() {
    /*var email1:String?=null
    var email2:String?=null
    var fullName:String?=null
    var contactNumber:String?=null
    var password:String?=null
    var reTypePassword:String?=null

    var idType:String?=null
    var idNumber:String?=null*/
    var fullName:MutableLiveData<String> = MutableLiveData()
    var email:MutableLiveData<String> = MutableLiveData()
    var reTypeEmail:MutableLiveData<String> = MutableLiveData()
    var contactNumber:MutableLiveData<String> = MutableLiveData()
    var dob:MutableLiveData<String> = MutableLiveData()
    var gender: Gender = Gender.M
    val selectedItemContactCode = ObservableInt()
    val selectedItemNationalityCode = ObservableInt()

    var listener:SimpleListener?=null


    fun onSignUp(){

        if(!fullName.value.isNullOrEmpty()and !email.value.isNullOrEmpty()) {
            if (email.value.equals(reTypeEmail.value)) {
                if (isValidEmail(email.value!!)) {
                        /*if (selectedItemIDType.get() == 0) {
                            idType = "MyKad"
                        } else {
                            idType = "Passport"
                        }*/

                        /*var user = User(
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
                        )*/

                            var user = User(
                                0,
                                fullName.value!!,
                                "",
                                email.value!!,
                                "",
                                contactNumber.value!!,
                                "",
                                "",
                                "",
                                gender.name,
                                "MY",
                                dob.value!!,
                                "",
                                "",
                                "",
                                "",
                                0,
                                System.currentTimeMillis(),
                            0,
                            System.currentTimeMillis(),
                            "",
                            "N")

                        signUpRepositary.saveUser(user)
                        listener?.onSuccess("success")
                } else {
                    listener?.onFailure("Your Email Address 1 is Invalid")
                }
            } else {
                listener?.onFailure("Email Mismatch")
            }
        }else{
            listener?.onFailure("All Field(s) Mandatory")
        }

    }
}