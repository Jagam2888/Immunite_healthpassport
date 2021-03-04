package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ImmunizationHistoryRepositary
import com.cmg.vaccine.viewmodel.ImmunizationHistoryViewModel

class ImmunizationHistoryViewModelFactory(
        private val repositary: ImmunizationHistoryRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImmunizationHistoryViewModel(repositary) as T
    }
}