package com.cmg.vaccine.repositary

import com.cmg.vaccine.model.request.AuthRequest
import com.cmg.vaccine.model.response.AuthResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest

class AuthRepositary(
    private val api:MyApi
) : SafeAPIRequest(){

    suspend fun loginUser(authReq : AuthRequest) : AuthResponse{
        return apiRequest {
            api.loginUser(authReq)
        }
    }
}