package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.AirasiaRepositary
import com.cmg.vaccine.viewmodel.AirasiaViewModel

class AirasiaViewModelFactory(
    private val repositary: AirasiaRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AirasiaViewModel(repositary) as T
    }
}