package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.OTPVerifyRepositary
import com.cmg.vaccine.viewmodel.OTPVerifyViewModel

class OTPVerifyModelFactory(
        private val repositary: OTPVerifyRepositary
):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OTPVerifyViewModel(repositary) as T
    }
}