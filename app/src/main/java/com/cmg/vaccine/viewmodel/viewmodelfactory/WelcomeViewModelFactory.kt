package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.WelcomeViewModelRepositary
import com.cmg.vaccine.viewmodel.WelcomeViewModel

class WelcomeViewModelFactory(
        private val repositary: WelcomeViewModelRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WelcomeViewModel(repositary) as T
    }
}