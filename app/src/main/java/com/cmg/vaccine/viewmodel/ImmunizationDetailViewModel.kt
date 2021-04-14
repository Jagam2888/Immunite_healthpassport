package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.VaccineReport
import com.cmg.vaccine.repositary.ImmunizationDetailRepositary

class ImmunizationDetailViewModel(
    private val repositary: ImmunizationDetailRepositary
):ViewModel() {

    var _vaccineReport = MutableLiveData<VaccineReport>()
    val vaccineReport:LiveData<VaccineReport>
    get() = _vaccineReport

    fun loadData(id:Int){
        val record = repositary.getVaccineReport(id)
        if (record != null){
            _vaccineReport.value = record
        }
    }
}