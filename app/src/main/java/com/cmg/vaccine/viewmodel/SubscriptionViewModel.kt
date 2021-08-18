package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.response.GetSubscriptionPackageData
import com.cmg.vaccine.repositary.SubscriptionRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import kotlinx.coroutines.launch

/**
 * Created by jagad on 8/17/2021
 */
class SubscriptionViewModel(
    private val subscriptionRepositary: SubscriptionRepositary
):ViewModel() {

    private val _packageList:MutableLiveData<List<GetSubscriptionPackageData>> = MutableLiveData()
    val packageList:LiveData<List<GetSubscriptionPackageData>>
        get() = _packageList

    var listener:SimpleListener?=null

    init {
        listener?.onStarted()
        viewModelScope.launch {
            Couritnes.main {
                try {
                    val response = subscriptionRepositary.getSubscriptionPackageApi()
                    if (!response.data.isNullOrEmpty()){
                        _packageList.postValue(response.data)
                    }
                    listener?.onSuccess("success")
                }catch (e:APIException){
                    listener?.onShowToast(e.message!!)
                }catch (e:NoInternetException){
                    listener?.onShowToast(e.message!!)
                }
            }
        }
    }
}