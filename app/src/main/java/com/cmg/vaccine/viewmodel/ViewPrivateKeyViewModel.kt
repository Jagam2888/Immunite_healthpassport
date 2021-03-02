package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.ViewPrivateKeyRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.changeDateFormatForPrivateKeyDecrypt
import my.com.immunitee.blockchainapi.utils.EncryptionUtils

import java.net.SocketTimeoutException

class ViewPrivateKeyViewModel(
    private val repositary: ViewPrivateKeyRepositary
):ViewModel() {

    val _privateKey:MutableLiveData<String> = MutableLiveData()

    val privateKey:LiveData<String>
        get() = _privateKey

    var listener:SimpleListener?=null

    var _userName:MutableLiveData<String> = MutableLiveData()
    val userName:LiveData<String>
    get() = _userName

    private fun decryptServerKey(pk:String,dob:String):String?{
        return EncryptionUtils.decrypt(pk,dob)
    }


    fun getPatientPrivateKey() {
        val getPrivateKey = repositary.getParentPrivateKey()
        var originalPrivateKey:String?=null
        listener?.onStarted()
        if (getPrivateKey.isNullOrEmpty()){
            Couritnes.main {
                try {
                    val response = repositary.getParentPrivateKeyFromAPI()
                    if (response.StatusCode == 1){
                        val getUser = repositary.getUserData()
                        if (getUser != null){
                            if (!response.PrivateKey.isNullOrEmpty()){
                                val tempDob = changeDateFormatForPrivateKeyDecrypt(getUser.dob!!)
                                val tempPK = response.PrivateKey
                                originalPrivateKey = decryptServerKey(tempPK,
                                    tempDob!!)
                            }
                            getUser.privateKey = originalPrivateKey
                            repositary.updateUser(getUser)
                        }
                        repositary.savePrivateKey(originalPrivateKey!!)
                        _privateKey.value = originalPrivateKey
                        listener?.onSuccess(response.Message)
                    }else{
                        listener?.onFailure(response.Message)
                    }

                }catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            listener?.onSuccess(getPrivateKey)
            _privateKey.value = getPrivateKey
        }
    }

    fun getDependentPrivateKey(subId:String) {
        val getPrivateKey = repositary.getDependentPrivateKey(subId)
        var originalPrivateKey:String?=null
        listener?.onStarted()
        if (getPrivateKey.isNullOrEmpty()){
            Couritnes.main {
                try {
                    val response = repositary.getDependentPrivateKeyFromAPI(subId)
                    if (response.StatusCode == 1){
                        val getUser = repositary.getDependentData(subId)
                        if (getUser != null){
                            if (!response.PrivateKey.isNullOrEmpty()){
                                val tempDob = changeDateFormatForPrivateKeyDecrypt(getUser.dob!!)
                                val tempPK = response.PrivateKey
                                originalPrivateKey = decryptServerKey(tempPK,
                                    tempDob!!)
                            }
                            getUser.privateKey = originalPrivateKey
                            repositary.updateDependent(getUser)
                        }
                        //repositary.savePrivateKey(response.PrivateKey)
                        _privateKey.value = originalPrivateKey
                        listener?.onSuccess(response.Message)
                    }else{
                        listener?.onFailure(response.Message)
                    }

                }catch (e: APIException) {
                    listener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure(e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onFailure(e.message!!)
                }
            }
        }else{
            listener?.onSuccess(getPrivateKey)
            _privateKey.value = getPrivateKey
        }
    }

    /*fun getPrivateKey(){
        val email = repositary.getEmail()

        val key = repositary.getPrivateKey(email!!)

        _privateKey.value = key
    }*/
}