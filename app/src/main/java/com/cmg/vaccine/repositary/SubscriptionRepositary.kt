package com.cmg.vaccine.repositary

import com.cmg.vaccine.model.response.GetSubscriptionPackage
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest

/**
 * Created by jagad on 8/17/2021
 */
class SubscriptionRepositary(
    private val api: MyApi
):SafeAPIRequest() {

    suspend fun getSubscriptionPackageApi():GetSubscriptionPackage{
        return apiRequest {
            api.getSubscriptionPackage()
        }
    }

}