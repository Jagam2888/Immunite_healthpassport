package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ChangePasswordRepositary
import com.cmg.vaccine.viewmodel.ChangePasswordViewModel


class ChangePasswordModelFactory(
        private val repositary: ChangePasswordRepositary
):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChangePasswordViewModel(repositary) as T
    }
}