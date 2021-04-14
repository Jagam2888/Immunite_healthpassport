package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.NotificationRepositary
import com.cmg.vaccine.viewmodel.NotificationViewModel

class NotificationViewModelFactory(
    private val repositary: NotificationRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationViewModel(repositary) as T
    }
}