package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.model.response.ViewReport
import com.cmg.vaccine.repositary.ViewReportListRepositary

class ViewReportListViewModel(
) : ViewModel() {

    val _viewReportList = MutableLiveData<List<ViewReport>>()

    val viewReport : LiveData<List<ViewReport>>
        get() = _viewReportList


    fun setViewReport(viewReport:List<ViewReport>){
        _viewReportList.value = viewReport
    }

    fun getViewReport(){

    }
}