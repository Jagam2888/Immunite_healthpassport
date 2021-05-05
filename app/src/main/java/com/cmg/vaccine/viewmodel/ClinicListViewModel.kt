package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.response.ClinicListResponseDataTwo
import com.cmg.vaccine.repositary.ClinicListRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import com.google.android.gms.common.api.ApiException
import java.lang.Exception

class ClinicListViewModel(
    private val repositary: ClinicListRepositary
):ViewModel() {

    var listener:SimpleListener?=null

    var _clinicList:MutableLiveData<List<ClinicListResponseDataTwo>> = MutableLiveData()
    val clincList:LiveData<List<ClinicListResponseDataTwo>>
    get() = _clinicList

    var errorMsg = ObservableField<String>()


    init {
        errorMsg.set("No Data")
        listener?.onStarted()
        Couritnes.main {
            try {
                val response = repositary.getClinicListApi()
                if (response.data.totalItems > 0){
                    if (!response.data.data.isNullOrEmpty()){
                        listener?.onSuccess("")
                        _clinicList.value = response.data.data
                    }else{
                        listener?.onFailure("1No Data")
                    }
                }
            }catch (e:ApiException){
                errorMsg.set(e.message)
                listener?.onFailure("2"+e.message)
            }catch (e:NoInternetException){
                errorMsg.set(e.message)
                listener?.onFailure("3"+e.message)
            }catch (e:Exception){
                errorMsg.set(e.message)
                listener?.onFailure("4"+e.message)
            }
        }
    }
}