package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.AuthRepositary
import com.cmg.vaccine.viewmodel.AuthViewModel

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(
    private val repositary:AuthRepositary
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelclass : Class<T>):T{
        return AuthViewModel(repositary) as T
    }
}