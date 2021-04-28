package com.cmg.vaccine.viewmodel

import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.response.SystemConfigResponseData
import com.cmg.vaccine.repositary.WelcomeViewModelRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import java.lang.Exception

class WelcomeViewModel(
        private val repositary: WelcomeViewModelRepositary
):ViewModel() {

    var listener:SimpleListener?=null


    init {
        repositary.saveNotificationStatus()
        getSystemConfigData()
    }


    private fun getSystemConfigData(){
        Couritnes.main {
            try {
                val response = repositary.getSystemConfigDataFromAPI()
                if (!response.data.isNullOrEmpty()){
                    repositary.deleteSystemConfigData()
                    response.data.forEach {
                        if (!it.sysReferredBy.equals("backend",false)) {
                            val systemConfigResponseData = SystemConfigResponseData(
                                    it.configSeqno,
                                    it.sysCreatedBy,
                                    it.sysCreatedDate,
                                    it.sysDescription,
                                    it.sysMappingKeyName,
                                    it.sysMappingValue,
                                    it.sysReferredBy,
                                    it.sysStatus,
                                    it.sysUpdatedBy,
                                    it.sysUpdatedDate
                            )
                            repositary.insertSystemConfigData(systemConfigResponseData)
                        }
                    }
                }
            }catch (e: APIException){
                listener?.onShowToast(e.message!!)
            }catch (e: NoInternetException){
                listener?.onFailure("3"+e.message!!)
            }catch (e: Exception){
                listener?.onShowToast(e.message!!)
            }
        }
    }
}