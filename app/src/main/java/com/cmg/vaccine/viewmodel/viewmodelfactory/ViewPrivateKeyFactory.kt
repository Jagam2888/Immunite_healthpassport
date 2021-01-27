package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.ViewPrivateKeyRepositary
import com.cmg.vaccine.viewmodel.ViewPrivateKeyViewModel

class ViewPrivateKeyFactory(
    private val repositary: ViewPrivateKeyRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewPrivateKeyViewModel(repositary) as T
    }
}