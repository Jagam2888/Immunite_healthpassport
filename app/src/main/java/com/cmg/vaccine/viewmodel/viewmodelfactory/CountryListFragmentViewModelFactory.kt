package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.CountryListFragmentRepositary
import com.cmg.vaccine.viewmodel.CountryListFragmentViewModel

class CountryListFragmentViewModelFactory(
        private val repositary: CountryListFragmentRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CountryListFragmentViewModel(repositary) as T
    }
}