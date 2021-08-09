package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.model.request.*
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.util.Passparams
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MyApi {

    /*@POST("authenticate")
    suspend fun loginUser(@Body authReq:AuthRequest):Response<AuthResponse>*/

    @POST(Passparams.PATIENT_REGISTRATION)
    suspend fun signUp(@Body signUpReq: SignUpReq):Response<PatientRegResponse>

    @POST(Passparams.EDIT_PATIENT_PROFILE_API)
    suspend fun updateProfile(@Body updateProfileReq: UpdateProfileReq):Response<UpdatePatientResponse>

    @POST(Passparams.DEPENDENT_REGISTRATION)
    suspend fun dependentSignUp(@Body dependentRegReq: DependentRegReq):Response<DependentRegResponse>

    @POST(Passparams.DEPENDENT_REGISTRATION)
    suspend fun existingDependent(@Body existingDependentReq: ExistingDependentReq):Response<DependentRegResponse>

    @POST(Passparams.EDIT_DEPENDENT_PROFILE_API)
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
    suspend fun verifyOTP(@Query("subsId")key: String,@Query("verifyTac")verifyTac:String,@Query("isSignup")isSignup:String):Response<OTPVerifiyResponse>

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

    @GET(Passparams.GET_ALL_WORLD_ENTRY_RULE)
    suspend fun getAllWorldEntryCountryRules():Response<WorldEntryRulesResponse>

    /*@POST(Passparams.IMMUNIZATION_HISTORY)
    suspend fun immunizationHistory(@Body immunizationHistoryReq: ImmunizationHistoryReq):Response<ImmunizationHistoryResponse>*/

    @Multipart
    @POST(Passparams.IMMUNIZATION_HISTORY)
    suspend fun immunizationHistory(@Part file:MultipartBody.Part,@Part("form") immunizationHistoryReq: ImmunizationHistoryReq):Response<ImmunizationHistoryResponse>

    @GET(Passparams.GET_TEST_REPORT_LIST)
    suspend fun getVaccineTestRef(@Query("privateKey")privateKey:String):Response<ResponseBody>

    @GET(Passparams.GET_VACCINE_REPORT_LIST)
    suspend fun getVaccineReport(@Query("privateKey")privateKey:String):Response<ResponseBody>

    @POST(Passparams.UPDATE_UUID)
    suspend fun updateUUID(@Query("subsId")subId:String,@Query("uuid") uuid:String):Response<UpdateUUIDResponse>

    @POST(Passparams.UPDATE_PRIVATE_KEY_STATUS)
    suspend fun updatePrivateKeyStatus(@Query("subsId")subId:String,@Query("status") uuid:String):Response<UpdateUUIDResponse>

    @POST(Passparams.UPDATE_DEPENDENT_PRIVATE_KEY_STATUS)
    suspend fun updateDependentPrivateKeyStatus(@Query("subsId")subId:String,@Query("status") uuid:String):Response<UpdateUUIDResponse>

    @POST(Passparams.UPDATE_FCM_TOKEN)
    suspend fun updateFCMToken(@Query("subsId")subid:String,@Query("token")token:String):Response<UpdateFCMTokenResponse>

    @GET(Passparams.GET_ALL_AIRPORT_CITIES)
    suspend fun getAllAirportCities():Response<GetAllAirportCitiesResponse>

    @GET
    @Streaming
    fun downLoadDynamicUrl(@Url fileUrl:String): Call<ResponseBody>

    @GET(Passparams.GET_IDENTIFIER_TYPE)
    suspend fun getIdentifierType():Response<IdentifierTypeResponse>

    @GET(Passparams.GET_TEST_CODES)
    suspend fun getTestCodes():Response<GetTestCodeResponse>

    @GET(Passparams.GET_WORLD_PRIORITY_LIST)
    suspend fun getWorldPriorityList():Response<GetWorldPriorityListResponse>

    @GET(Passparams.BLOCK_CHAIN_ERROR_CODE)
    suspend fun getBlockChainErrorCode():Response<BlockChainErrorCodeResponse>

    @GET(Passparams.SYSTEM_CONFIG)
    suspend fun getSystemConfigData():Response<SystemConfigResponse>

    @GET(Passparams.GET_CLINIC_LIST)
    suspend fun getClinicList():Response<ClinicListResponse>

    @GET(Passparams.OBSERVATION_STATUS_CODE)
    suspend fun getObservationStatusCode():Response<ObservationStatusResponse>

    @POST(Passparams.REMOVE_DEPENDENT)
    suspend fun removeDependent(@Query("masterSubsId")masterSubId:String,@Query("dependentSubsId")subId:String):Response<RemoveDependentResponse>

    @POST(Passparams.WEB_CHECK_IN_API)
    suspend fun webCheckInAPI(@Body webCheckInReq: WebCheckInReq):Response<WebCheckInResponse>

    @GET(Passparams.PACKAGECODE)
    suspend fun searchPackageCode():Response<PackageCodeResponse>

    @Multipart
    @POST(Passparams.ADD_FEEDBACK)
    suspend fun addFeedback(@Part file: Array<MultipartBody.Part?>,
                            @Part("form") addFeedbackReq: AddFeedbackReq):Response<AddFeedbackResponse>

    @GET(Passparams.GET_FEEDBACK)
    suspend fun getFeedBackListAPI(@Query("casePrincpleId")subId: String):Response<GetFeedbackStatusResponse>




    companion object{
        operator fun invoke(
            context: Context
        ) : MyApi{
            return RetrofitClientInstance(context).create(MyApi::class.java)
        }
    }
}