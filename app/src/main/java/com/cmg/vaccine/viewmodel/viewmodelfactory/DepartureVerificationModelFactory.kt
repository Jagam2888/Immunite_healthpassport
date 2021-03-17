package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.DepartureVerificationRepositary
import com.cmg.vaccine.viewmodel.DepartureVerificationViewModel

class DepartureVerificationModelFactory(
    private val repositary: DepartureVerificationRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DepartureVerificationViewModel(repositary) as T
    }
}