package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.model.request.AuthRequest
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.response.AuthResponse
import com.cmg.vaccine.model.response.RegisterResponse
import com.cmg.vaccine.prefernces.PreferenceProvider
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyApi {

    @POST("authenticate")
    suspend fun loginUser(@Body authReq:AuthRequest):Response<AuthResponse>

    @POST("nhr-module-user/patientsign")
    suspend fun signUp(@Body signUpReq: SignUpReq):Response<RegisterResponse>

    companion object{
        operator fun invoke(
            context: Context,
        preferenceProvider: PreferenceProvider
        ) : MyApi{
            return RetrofitClientInstance(context,preferenceProvider).create(MyApi::class.java)
        }
    }
}