package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.User
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.request.SignUpReqData
import com.cmg.vaccine.repositary.TellUsRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class TellUsMoreViewModel(
    private val repositary: TellUsRepositary
):ViewModel() {

    var address:MutableLiveData<String> = MutableLiveData()
    var city:MutableLiveData<String> = MutableLiveData()
    var state:MutableLiveData<String> = MutableLiveData()
    var passportNo:MutableLiveData<String> = MutableLiveData()
    var idNo:MutableLiveData<String> = MutableLiveData()

    var isTermsRead:MutableLiveData<Boolean> = MutableLiveData()
    val selectedItemContactCode = ObservableInt()
    var isChecked = ObservableBoolean()

    var listener:SimpleListener?=null

    fun onRegister(){
        if(!passportNo.value.isNullOrEmpty()) {
            if (isChecked.get()) {
                var alreadyStored = repositary.getUserData()
                val gson = Gson()
                val type: Type = object : TypeToken<User>() {}.type
                var userData = gson.fromJson<User>(alreadyStored, type)

                /*val phoneCode = view.context.resources.getStringArray(R.array.code)
                var code = phoneCode.get(selectedItemContactCode.get())*/

                userData.countryCode = "MY"
                userData.passportNumber = passportNo.value!!
                userData.patientIdNo = idNo.value
                userData.address = address.value
                userData.city = city.value
                userData.state = state.value
                userData.patientSeqno = 0

                repositary.saveUserEmail(userData.email)


                val signUpReq = SignUpReq()
                var signUpReqData = SignUpReqData()

                signUpReqData.email = userData.email
                signUpReqData.firstName = userData.fullName
                signUpReqData.mobileNumber = userData.mobileNumber
                signUpReqData.gender = userData.gender
                signUpReqData.dob = userData.dob
                signUpReqData.countryCode = userData.countryCode
                signUpReqData.residentialAddress = userData.address
                signUpReqData.townCity = userData.city
                signUpReqData.provinceState = userData.state
                signUpReqData.passportNo = userData.passportNumber
                signUpReqData.idNo = userData.patientIdNo
                signUpReqData.nationalityCountry = userData.nationality

                signUpReq.data = signUpReqData

                Couritnes.main {
                    try {
                        val response = repositary.signUp(signUpReq)
                        if (response.StatusCode == 1 && !response.ParentPrivateKey.isNullOrEmpty()) {
                            userData.privateKey = response.ParentPrivateKey
                            repositary.insertUser(userData)
                            listener?.onSuccess(response.Message)
                        }else{
                            listener?.onFailure(response.Message)
                        }
                    } catch (e: APIException) {
                        listener?.onFailure(e.message!!)
                    } catch (e: NoInternetException) {
                        listener?.onFailure(e.message!!)
                    } catch (e: SocketTimeoutException) {
                        listener?.onFailure(e.message!!)
                    }
                }
            } else {
                listener?.onFailure("Please Read Terms and Condtition")
            }
        }else{
            listener?.onFailure("Passport Number Missing")
        }
    }
}