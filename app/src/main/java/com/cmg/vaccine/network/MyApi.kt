package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.ImmunizationHistoryReq
import com.cmg.vaccine.model.request.SignUpReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.util.Passparams
import okhttp3.ResponseBody
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

    @GET(Passparams.GET_EXISTING_USER)
    suspend fun getExistingUser(@Query("privateKey") privateKey:String):Response<ResponseBody>

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

    @GET(Passparams.TESTTYPE)
    suspend fun getTestType():Response<TestTypeResponse>

    @GET(Passparams.VIRUS)
    suspend fun getVirusList():Response<VirusListResponse>

    @GET(Passparams.VACCINE)
    suspend fun getVaccineDetailList():Response<VaccineDetailListResponse>

    @GET(Passparams.GET_PATIENT_PRIVATE_KEY)
    suspend fun getParentPrivateKey(@Query("subsId") subsId:String):Response<GetPrivateKeyResponse>

    @GET(Passparams.GET_DEPENDENT_PRIVATE_KEY)
    suspend fun getDependentPrivateKey(@Query("subsId") subsId:String):Response<GetPrivateKeyResponse>

    @GET(Passparams.GET_TEST_REPORT_LIST)
    suspend fun getTestReportList(@Query("privateKey") key:String):Response<TestReportResponseBlockChain>

    @GET(Passparams.GET_VACCINE_LIST)
    suspend fun getVaccineList(@Query("privateKey") key:String):Response<VaccineResponseBlockChain>

    @GET(Passparams.GET_WORLD_ENTRIES_LIST)
    suspend fun getWorldEntriesCountryList():Response<WorldEntriesCountryList>

    @GET(Passparams.GET_WORLD_ENTRIES_RULES)
    suspend fun getWorldEntryCountryRules(@Query("countryCode")countryCode:String):Response<WorldEntryRulesResponse>

    @POST(Passparams.IMMUNIZATION_HISTORY)
    suspend fun immunizationHistory(@Body immunizationHistoryReq: ImmunizationHistoryReq):Response<ImmunizationHistoryResponse>

    @GET(Passparams.GET_VACCINE_TEST_REF)
    suspend fun getVaccineTestRef(@Query("privateKey")privateKey:String):Response<ResponseBody>

    @POST(Passparams.UPDATE_UUID)
    suspend fun updateUUID(@Query("subsId")subId:String,@Query("uuid") uuid:String):Response<UpdateUUIDResponse>

    @POST(Passparams.UPDATE_PRIVATE_KEY_STATUS)
    suspend fun updatePrivateKeyStatus(@Query("subsId")subId:String,@Query("status") uuid:String):Response<UpdateUUIDResponse>

    companion object{
        operator fun invoke(
            context: Context
        ) : MyApi{
            return RetrofitClientInstance(context).create(MyApi::class.java)
        }
    }
}