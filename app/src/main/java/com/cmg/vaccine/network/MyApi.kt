package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.model.request.AuthRequest
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.util.Passparams
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyApi {

    /*@POST("authenticate")
    suspend fun loginUser(@Body authReq:AuthRequest):Response<AuthResponse>*/

    @POST(Passparams.PATIENT_REGISTRATION)
    suspend fun signUp(@Body signUpReq: SignUpReq):Response<PatientRegResponse>

    @POST(Passparams.EDIT_PATIENT_PROFILE)
    suspend fun updateProfile(@Body updateProfileReq: UpdateProfileReq):Response<UpdatePatientResponse>

    @POST(Passparams.DEPENDENT_REGISTRATION)
    suspend fun dependentSignUp(@Body dependentRegReq: DependentRegReq):Response<DependentRegResponse>

    @POST(Passparams.EDIT_DEPENDENT_PROFILE)
    suspend fun updateDependentProfile(@Body updateProfileReq: UpdateProfileReq):Response<DependentRegResponse>

    @GET(Passparams.SEARCH_PRIVATE_KEY)
    suspend fun searchPatientVaccine(@Query("privateKey") key:String ):Response<VaccineResponse>

    @GET(Passparams.SEARCH_VACCINE)
    suspend fun searchVaccineList(@Query("subsId")key: String):Response<VaccineListResponse>

    @GET(Passparams.SEARCH_TEST_REPORT)
    suspend fun searchTestReportList(@Query("subsId")key: String):Response<TestReportListResponse>

    @POST(Passparams.VERIFY_TAC)
    suspend fun verifyOTP(@Query("subsId")key: String,@Query("verifyTac")verifyTac:String):Response<OTPVerifiyResponse>

    @POST(Passparams.RESEND_TAC)
    suspend fun resendOTP(@Query("subsId")key: String):Response<ResentOTPResponse>

    @GET(Passparams.COUNTRIES)
    suspend fun getAllCountries():Response<CountryResponse>

    companion object{
        operator fun invoke(
            context: Context,
        preferenceProvider: PreferenceProvider
        ) : MyApi{
            return RetrofitClientInstance(context,preferenceProvider).create(MyApi::class.java)
        }
    }
}