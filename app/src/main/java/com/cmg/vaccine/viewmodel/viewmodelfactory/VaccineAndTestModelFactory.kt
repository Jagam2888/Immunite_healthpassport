package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.VaccineAndTestRepositary
import com.cmg.vaccine.viewmodel.VaccineAndTestViewModel

class VaccineAndTestModelFactory(
    private val repositary: VaccineAndTestRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VaccineAndTestViewModel(repositary) as T
    }
}