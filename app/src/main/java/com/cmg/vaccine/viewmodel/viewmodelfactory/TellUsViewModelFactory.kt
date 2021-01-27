package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.TellUsRepositary
import com.cmg.vaccine.viewmodel.TellUsMoreViewModel

class TellUsViewModelFactory(
    private val repositary: TellUsRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TellUsMoreViewModel(repositary) as T
    }
}