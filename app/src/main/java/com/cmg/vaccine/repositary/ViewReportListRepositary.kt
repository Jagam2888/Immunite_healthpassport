package com.cmg.vaccine.repositary

import androidx.lifecycle.LiveData
import com.cmg.vaccine.model.response.ViewReport
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.prefernces.PreferenceProvider

class ViewReportListRepositary(
        private val api: MyApi,
        private val preferenceProvider: PreferenceProvider
) {

    /*fun getViewReport():LiveData<List<ViewReport>>{

    }*/
}