package com.cmg.vaccine.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.SubscriptionRepositary
import com.cmg.vaccine.viewmodel.SubscriptionViewModel

/**
 * Created by jagad on 8/17/2021
 */
class SubscriptionViewModelFactory(
    private val subscriptionRepositary: SubscriptionRepositary
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SubscriptionViewModel(subscriptionRepositary) as T
    }
}