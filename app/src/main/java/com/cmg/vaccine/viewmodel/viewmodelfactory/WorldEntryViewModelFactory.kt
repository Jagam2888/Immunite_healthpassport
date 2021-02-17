package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.WorldEntryRepositary
import com.cmg.vaccine.viewmodel.WorldEntryViewModel

class WorldEntryViewModelFactory(
    private val repositary: WorldEntryRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorldEntryViewModel(repositary) as T
    }
}