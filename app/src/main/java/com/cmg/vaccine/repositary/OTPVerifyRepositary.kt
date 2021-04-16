package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.DependentRegResponse
import com.cmg.vaccine.model.response.OTPVerifiyResponse
import com.cmg.vaccine.model.response.ResentOTPResponse
import com.cmg.vaccine.model.response.UpdatePatientResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class OTPVerifyRepositary(
        private val api: MyApi,
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun getDependent(subsId:String): Dependent {
        return database.getDao().getDependent(subsId)
    }

    fun updateVerifyStatus(user: User):Int{
        return database.getDao().updateVerifyStatus(user)
    }

    fun getPatientSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun updateUser(user: User):Int{
        return database.getDao().updateUser(user)
    }

    fun updateDependent(dependent: Dependent):Int{
        return database.getDao().updateDependent(dependent)
    }

    fun getUserDataPref():String?{
        return preferenceProvider.getUserReqData()
    }

    suspend fun OTPVerify(key:String,tacCode:String,isSignup:String):OTPVerifiyResponse{
        return apiRequest {
            api.verifyOTP(key,tacCode,isSignup)
        }
    }

    suspend fun resendOTP(key: String):ResentOTPResponse{
        return apiRequest {
            api.resendOTP(key)
        }
    }

    fun getEditProfileReq():String?{
        return preferenceProvider.getEditProfileReq()
    }

    fun getAddDependent():String?{
        return preferenceProvider.getaddDependentReq()
    }

    suspend fun updateProfile(updateProfileReq: UpdateProfileReq): UpdatePatientResponse {
        return apiRequest {
            api.updateProfile(updateProfileReq)
        }
    }

    suspend fun dependentSignUp(dependentRegReq: DependentRegReq):DependentRegResponse{
        return apiRequest {
            api.dependentSignUp(dependentRegReq)
        }
    }

    fun getParentSubId():String?{
        return preferenceProvider.getSubId()
    }

    suspend fun updateDependentProfile(updateProfileReq: UpdateProfileReq): DependentRegResponse {
        return apiRequest {
            api.updateDependentProfile(updateProfileReq)
        }
    }

    suspend fun insertDependentSignUp(dependent: Dependent):Long{
        return database.getDao().insertDependent(dependent)
    }
}