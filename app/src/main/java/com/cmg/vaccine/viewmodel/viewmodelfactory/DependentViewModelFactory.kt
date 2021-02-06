package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.DependentRepositary
import com.cmg.vaccine.viewmodel.DependentViewModel

class DependentViewModelFactory(
    private val repositary: DependentRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DependentViewModel(repositary) as T
    }
}