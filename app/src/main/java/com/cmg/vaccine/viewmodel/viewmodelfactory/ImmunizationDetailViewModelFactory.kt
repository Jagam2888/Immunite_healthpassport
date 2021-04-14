package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ImmunizationDetailRepositary
import com.cmg.vaccine.viewmodel.ImmunizationDetailViewModel

class ImmunizationDetailViewModelFactory(
    private val repositary: ImmunizationDetailRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImmunizationDetailViewModel(repositary) as T
    }
}