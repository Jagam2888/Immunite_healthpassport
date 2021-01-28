package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ViewReportListRepositary
import com.cmg.vaccine.viewmodel.ViewReportListViewModel

class ViewReportModelFactory(
        private val repositary: ViewReportListRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewReportListViewModel(repositary) as T
    }
}