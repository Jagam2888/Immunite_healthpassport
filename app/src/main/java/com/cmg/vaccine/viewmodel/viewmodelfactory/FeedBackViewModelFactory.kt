package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.FeedBackViewRepositary
import com.cmg.vaccine.viewmodel.FeedBackViewModel

class FeedBackViewModelFactory(
    private val repositary: FeedBackViewRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FeedBackViewModel(repositary) as T
    }
}