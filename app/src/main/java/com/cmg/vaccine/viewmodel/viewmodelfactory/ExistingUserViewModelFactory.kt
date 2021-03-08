package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ExistingUserRepositary
import com.cmg.vaccine.viewmodel.ExistingUserViewModel

class ExistingUserViewModelFactory(
    private val repositary: ExistingUserRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExistingUserViewModel(repositary) as T
    }
}