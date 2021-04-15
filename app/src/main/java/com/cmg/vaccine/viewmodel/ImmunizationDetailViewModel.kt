package com.cmg.vaccine.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.VaccineReport
import com.cmg.vaccine.repositary.ImmunizationDetailRepositary
import com.cmg.vaccine.util.changeDateFormatForVaccine

class ImmunizationDetailViewModel(
    private val repositary: ImmunizationDetailRepositary
):ViewModel() {

    var _vaccineReport = MutableLiveData<VaccineReport>()
    val vaccineReport:LiveData<VaccineReport>
    get() = _vaccineReport

    var displayDate = ObservableField<String>()
    var expireyDate = ObservableField<String>()

    fun loadData(id:Int){
        val record = repositary.getVaccineReport(id)
        if (record != null){
            /*if (!record.vaccineDisplayDate.isNullOrEmpty()){
                displayDate.set(changeDateFormatForVaccine(record.vaccineDisplayDate!!))
            }

            if (!record.expiryDate.isNullOrEmpty()){
                expireyDate.set(changeDateFormatForVaccine(record.expiryDate!!))
            }*/

            _vaccineReport.value = record
        }
    }
}