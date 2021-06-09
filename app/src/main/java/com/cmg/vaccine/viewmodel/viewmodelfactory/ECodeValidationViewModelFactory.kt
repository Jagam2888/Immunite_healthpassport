package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ECodeValidationRepositary
import com.cmg.vaccine.viewmodel.ECodeValidationViewModel

class ECodeValidationViewModelFactory(
    private val repositary: ECodeValidationRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ECodeValidationViewModel(repositary) as T
    }
}