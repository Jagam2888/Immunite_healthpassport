package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.LoginPinRepositary
import com.cmg.vaccine.viewmodel.LoginPinViewModel

class LoginPinViewFactory(
        private val repositary: LoginPinRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginPinViewModel(repositary) as T
    }
}