package com.cmg.vaccine.repositary

import com.cmg.vaccine.model.request.WebCheckInReq
import com.cmg.vaccine.model.response.WebCheckInResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest

class ECodeValidationRepositary(
    private val api: MyApi
):SafeAPIRequest() {

    suspend fun webCheckInApi(webCheckInReq: WebCheckInReq): WebCheckInResponse {
        return apiRequest {
            api.webCheckInAPI(webCheckInReq)
        }
    }
}