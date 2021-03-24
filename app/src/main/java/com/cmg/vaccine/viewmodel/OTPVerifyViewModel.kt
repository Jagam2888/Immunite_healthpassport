package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.repositary.OTPVerifyRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.Passparams
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class OTPVerifyViewModel(
        private val repositary: OTPVerifyRepositary
):ViewModel() {

    var pinTxt:MutableLiveData<String> = MutableLiveData()
    var listener:SimpleListener?=null

    //val isExistUser:MutableLiveData<Boolean> = MutableLiveData()
    var navigateFrom = ObservableField<String>()

    var _txtOTP:MutableLiveData<String> = MutableLiveData()
    val txtOTP:LiveData<String>
    get() = _txtOTP

    var userSubId:MutableLiveData<String> = MutableLiveData()

    var displaymobileNumber = ObservableField<String>()

    init {
        val userData = repositary.getUserData()
        if (userData != null){
            if (!userData.mobileNumber.isNullOrEmpty()) {
                var mobileNumberTest = userData.mobileNumber
                if (mobileNumberTest.length > 8) {
                    val myName = StringBuilder(mobileNumberTest)
                    myName.setCharAt(2, 'x')
                    myName.setCharAt(3, 'x')
                    myName.setCharAt(4, 'x')
                    myName.setCharAt(5, 'x')
                    mobileNumberTest = myName.toString()
                }
                displaymobileNumber.set("+" + userData.countryCode + mobileNumberTest)
                Log.d("mobilenumber", displaymobileNumber.get()!!)
            }
        }
        callOTPTac()
    }


    fun callOTPTac(){
        listener?.onStarted()
        Couritnes.main {
            try {
                val response = repositary.resendOTP(userSubId.value!!)
                //if (isResend)
                listener?.onFailure(response.Message)
                /*if (response.success){
                    listener?.onSuccess(response.message)
                }else{
                    listener?.onSuccess(response.message)
                }*/
            }catch (e: APIException) {
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }
        }
    }

    fun onClick(){
        listener?.onStarted()
        Couritnes.main {
            if (!pinTxt.value.isNullOrEmpty()) {
                try {
                    val response = repositary.OTPVerify(userSubId.value!!, pinTxt.value!!)
                    if (response.success){
                        if (navigateFrom.get().equals(Passparams.SIGNUP)) {
                            val userData = repositary.getUserData()
                            if (userData != null) {
                                userData.virifyStatus = "Y"
                                repositary.updateVerifyStatus(userData)
                            }
                            listener?.onSuccess(response.message)
                        }else if (navigateFrom.get().equals(Passparams.EDIT_PROFILE)){
                            onEditProfile()
                        }else if (navigateFrom.get().equals(Passparams.EDIT_DEPENDENT_PROFILE)){
                            onEditDependentProfile()
                        }else if (navigateFrom.get().equals(Passparams.FORGOT_PIN)){
                            listener?.onSuccess(response.message)
                        }
                    }else{
                        listener?.onFailure(response.message)
                    }
                } catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                }catch (e: NoInternetException){
                    listener?.onFailure(e.message!!)
                }catch (e: SocketTimeoutException){
                    listener?.onFailure(e.message!!)
                }
            }else{
                listener?.onFailure("Please enter your OTP")
            }
        }
    }

    private fun onEditProfile(){

        val editProfileDataValue = repositary.getEditProfileReq()
        val gson = Gson()
        val type: Type = object : TypeToken<UpdateProfileReq>() {}.type
        var editProfileData = gson.fromJson<UpdateProfileReq>(editProfileDataValue, type)
        var dob = editProfileData.data?.dob
        var dobTime = ""
        val dobTimeArray = editProfileData.data?.dob?.split(" ")
        if (dobTimeArray?.size!! > 1){
            dob = dobTimeArray[0]
            dobTime = dobTimeArray[1].dropLast(3)
        }
        Couritnes.main {
            try {
                val response = repositary.updateProfile(editProfileData)
                if (response.StatusCode == 1){
                    var user = repositary.getUserData()
                    user.gender = editProfileData.data?.gender.toString()
                    user.fullName = editProfileData.data?.firstName.toString()
                    user.mobileNumber = editProfileData.data?.mobileNumber.toString()
                    user.passportNumber = editProfileData.data?.passportNo.toString()
                    user.passportExpiryDate = editProfileData.data?.passportExpiryDate
                    user.patientIdNo = editProfileData.data?.idNo.toString()
                    user.dob = dob
                    user.email = editProfileData.data?.email.toString()
                    user.dobTime = dobTime
                    user.placeBirth = editProfileData.data?.placeOfBirth.toString()
                    user.nationality = editProfileData.data?.nationalityCountry.toString()
                    user.patientIdType = editProfileData.data?.idType.toString()
                    repositary.updateUser(user)
                    listener?.onSuccess(response.Message)
                }else{
                    listener?.onFailure(response.Message)
                }
            }catch (e: APIException) {
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }

    private fun onEditDependentProfile(){

        val editProfileDataValue = repositary.getEditProfileReq()
        val gson = Gson()
        val type: Type = object : TypeToken<UpdateProfileReq>() {}.type
        var editProfileData = gson.fromJson<UpdateProfileReq>(editProfileDataValue, type)
        var dob = editProfileData.data?.dob
        var dobTime = ""
        val dobTimeArray = editProfileData.data?.dob?.split(" ")
        if (dobTimeArray?.size!! > 1){
            dob = dobTimeArray[0]
            dobTime = dobTimeArray[1].dropLast(3)
        }
        Couritnes.main {
            try {
                val response = repositary.updateDependentProfile(editProfileData)
                if (response.StatusCode == 1){
                    var dependent = repositary.getDependent(userSubId.value!!)
                    dependent?.dob = dob
                    dependent?.dobTime = dobTime
                    dependent?.firstName = editProfileData.data?.firstName
                    dependent?.idNo = editProfileData.data?.idNo
                    dependent?.idType = editProfileData.data?.idType
                    dependent?.passportNo = editProfileData.data?.passportNo
                    dependent?.passportExpiryDate = editProfileData.data?.passportExpiryDate
                    dependent?.mobileNumber = editProfileData.data?.mobileNumber
                    dependent?.relationship = editProfileData.data?.relationship
                    dependent?.gender = editProfileData.data?.gender
                    dependent?.placeOfBirth = editProfileData.data?.placeOfBirth
                    dependent?.nationalityCountry = editProfileData.data?.nationalityCountry
                    dependent?.email = editProfileData.data?.email
                    dependent?.countryCode = editProfileData.data?.countryCode

                    repositary.updateDependent(dependent!!)
                    listener?.onSuccess(response.Message)
                }else{
                    listener?.onFailure(response.Message)
                }
            }catch (e: APIException) {
                listener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }catch (e:Exception){
                listener?.onFailure(e.message!!)
            }
        }
    }
}