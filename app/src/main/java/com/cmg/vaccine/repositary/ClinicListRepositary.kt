package com.cmg.vaccine.repositary

import com.cmg.vaccine.model.response.ClinicListResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest

class ClinicListRepositary(
    private val api: MyApi,
):SafeAPIRequest() {


    suspend fun getClinicListApi():ClinicListResponse{
        return apiRequest {
            api.getClinicList()
        }
    }
}