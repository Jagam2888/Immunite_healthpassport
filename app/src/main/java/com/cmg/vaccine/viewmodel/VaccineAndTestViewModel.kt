package com.cmg.vaccine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.User
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.repositary.VaccineAndTestRepositary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class VaccineAndTestViewModel(
    private val repositary: VaccineAndTestRepositary
):ViewModel() {

    val _testReport:MutableLiveData<TestReport> = MutableLiveData()
    val testReport:LiveData<TestReport>
    get() = _testReport

    fun loadData(value:String){
        /*val data = repositary.gettestReport(id)
        if (data != null){
            _testReport.value = data
        }*/
        val gson = Gson()
        val type: Type = object : TypeToken<TestReport>() {}.type
        var testReportData = gson.fromJson<TestReport>(value, type)
        _testReport.value = testReportData
    }
}