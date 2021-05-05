package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ClinicListRepositary
import com.cmg.vaccine.viewmodel.ClinicListViewModel

class ClinicListViewModelFactory(
    private val repositary: ClinicListRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ClinicListViewModel(repositary) as T
    }
}