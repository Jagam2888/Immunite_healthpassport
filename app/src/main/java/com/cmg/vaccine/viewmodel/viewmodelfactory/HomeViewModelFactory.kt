package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.HomeRepositary
import com.cmg.vaccine.viewmodel.HomeViewModel

class HomeViewModelFactory(
        private val repositary: HomeRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repositary) as T
    }
}