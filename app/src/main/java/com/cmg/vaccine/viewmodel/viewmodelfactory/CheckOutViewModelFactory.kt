package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.CheckOutRepositary
import com.cmg.vaccine.viewmodel.CheckOutViewModel

class CheckOutViewModelFactory(
    private val repositary: CheckOutRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CheckOutViewModel(repositary) as T
    }
}