package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.model.request.AuthRequest
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.prefernces.PreferenceProvider
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyApi {

    @POST("authenticate")
    suspend fun loginUser(@Body authReq:AuthRequest):Response<AuthResponse>

    @POST("nhr-module-user/patientreg")
    suspend fun signUp(@Body signUpReq: SignUpReq):Response<PatientRegResponse>

    @POST("nhr-module-user/editpatientprofile")
    suspend fun updateProfile(@Body updateProfileReq: UpdateProfileReq):Response<PatientRegResponse>

    @POST("nhr-module-user/patientdependentreg")
    suspend fun dependentSignUp(@Body dependentRegReq: DependentRegReq):Response<DependentRegResponse>

    @POST("nhr-module-user/editdependentProfile")
    suspend fun updateDependentProfile(@Body updateProfileReq: UpdateProfileReq):Response<DependentRegResponse>

    @GET("nhr-module-user/searchPrivateKey")
    suspend fun searchPatientVaccine(@Query("privateKey") key:String ):Response<VaccineResponse>

    @GET("nhr-module-user/searchVaccine")
    suspend fun searchVaccineList(@Query("privateKey")key: String):Response<VaccineListResponse>

    @GET("nhr-module-user/verifyTac")
    suspend fun verifyOTP(@Query("privateKey")key: String,@Query("verifyTac")verifyTac:String)

    companion object{
        operator fun invoke(
            context: Context,
        preferenceProvider: PreferenceProvider
        ) : MyApi{
            return RetrofitClientInstance(context,preferenceProvider).create(MyApi::class.java)
        }
    }
}