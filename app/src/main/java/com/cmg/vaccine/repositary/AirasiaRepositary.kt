package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.model.request.WebCheckInReq
import com.cmg.vaccine.model.response.WebCheckInResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest

class AirasiaRepositary(
    private val api:MyApi
):SafeAPIRequest() {

    suspend fun webCheckInApi(webCheckInReq: WebCheckInReq): WebCheckInResponse {
        return apiRequest {
            api.webCheckInAPI(webCheckInReq)
        }
    }
}