package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.SignUpRepositary
import com.cmg.vaccine.viewmodel.SignupViewModel

class SignUpModelFactory(
    private val repositary: SignUpRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignupViewModel(repositary) as T
    }
}