package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.SettingsRepositary
import com.cmg.vaccine.viewmodel.SettingsViewModel

class SettingsModelFactory(
    private val repositary: SettingsRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(repositary) as T
    }
}