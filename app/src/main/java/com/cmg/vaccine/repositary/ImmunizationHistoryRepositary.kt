package com.cmg.vaccine.repositary

import com.cmg.vaccine.model.request.ImmunizationHistoryReq
import com.cmg.vaccine.model.response.ImmunizationHistoryResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import okhttp3.MultipartBody

class ImmunizationHistoryRepositary(
        private val api: MyApi,
):SafeAPIRequest() {

    suspend fun immunizationHistory(file:Array<MultipartBody.Part?>,immunizationHistoryReq: ImmunizationHistoryReq):ImmunizationHistoryResponse{
        return apiRequest {
            api.immunizationHistory(file,immunizationHistoryReq)
        }
    }
}