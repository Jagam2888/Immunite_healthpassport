package com.cmg.vaccine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.SplashRepositary

@Suppress("UNCHECKED_CAST")
class SplashModelFactory(
    private val repositary:SplashRepositary
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelclass : Class<T>):T{
        return SplashViewModel(repositary) as T
    }
}