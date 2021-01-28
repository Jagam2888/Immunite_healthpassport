package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.response.VaccineListResponseData
import com.cmg.vaccine.model.response.ViewReport
import com.cmg.vaccine.repositary.ViewReportListRepositary
import com.cmg.vaccine.util.APIException
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.NoInternetException
import java.net.SocketTimeoutException

class ViewReportListViewModel(
        private val repositary: ViewReportListRepositary
) : ViewModel() {

    val _viewReportList = MutableLiveData<List<VaccineListResponseData>>()

    val viewReport : LiveData<List<VaccineListResponseData>>
        get() = _viewReportList

    var listSize = ObservableInt()


    /*fun setViewReport(viewReport:List<ViewReport>){
        _viewReportList.value = viewReport
    }

    fun getViewReport(){

    }*/
    var listener:SimpleListener?=null

    init {
        listener?.onStarted()
        Couritnes.main {
            try {
                val privateKey = repositary.getPrivateKey()
                val response = repositary.getVaccineList(privateKey!!)
                listSize.set(response.data.size)
                if (response.data.isNotEmpty()) {
                    _viewReportList.value = response.data
                }
            }catch (e:APIException){
                listener?.onFailure(e.message!!)
            }catch (e:NoInternetException){
                listener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                listener?.onFailure(e.message!!)
            }
        }
    }
}