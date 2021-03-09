package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.RestoreBackupOptionListRepositary
import com.cmg.vaccine.viewmodel.RestoreBackupOptionListViewModel

class RestoreBackupOptionListViewModelFactory(
        private val repositary: RestoreBackupOptionListRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RestoreBackupOptionListViewModel(repositary) as T
    }
}