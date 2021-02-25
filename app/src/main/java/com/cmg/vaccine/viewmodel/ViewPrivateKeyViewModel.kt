package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.ViewPrivateKeyRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
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


    fun getPatientPrivateKey() {
        val getPrivateKey = repositary.getParentPrivateKey()
        listener?.onStarted()
        if (getPrivateKey.isNullOrEmpty()){
            Couritnes.main {
                try {
                    val response = repositary.getParentPrivateKeyFromAPI()
                    if (response.StatusCode == 1){
                        val getUser = repositary.getUserData()
                        if (getUser != null){
                            getUser.privateKey = response.PrivateKey
                            repositary.updateUser(getUser)
                        }
                        repositary.savePrivateKey(response.PrivateKey)
                        _privateKey.value = response.PrivateKey
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
        listener?.onStarted()
        if (getPrivateKey.isNullOrEmpty()){
            Couritnes.main {
                try {
                    val response = repositary.getDependentPrivateKeyFromAPI(subId)
                    if (response.StatusCode == 1){
                        val getUser = repositary.getDependentData(subId)
                        if (getUser != null){
                            getUser.privateKey = response.PrivateKey
                            repositary.updateDependent(getUser)
                        }
                        //repositary.savePrivateKey(response.PrivateKey)
                        _privateKey.value = response.PrivateKey
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