package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ProfileRepositary
import com.cmg.vaccine.viewmodel.ProfileViewModel

class ProfileViewModelFactory(
    private val repositary: ProfileRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(repositary) as T
    }
}