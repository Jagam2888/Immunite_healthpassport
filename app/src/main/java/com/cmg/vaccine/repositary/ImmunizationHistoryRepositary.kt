package com.cmg.vaccine.repositary

import com.cmg.vaccine.model.request.ImmunizationHistoryReq
import com.cmg.vaccine.model.response.ImmunizationHistoryResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest

class ImmunizationHistoryRepositary(
        private val api: MyApi,
):SafeAPIRequest() {

    suspend fun immunizationHistory(immunizationHistoryReq: ImmunizationHistoryReq):ImmunizationHistoryResponse{
        return apiRequest {
            api.immunizationHistory(immunizationHistoryReq)
        }
    }
}