package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.response.OTPVerifiyResponse
import com.cmg.vaccine.model.response.ResentOTPResponse
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

    fun updateVerifyStatus(user: User):Int{
        return database.getDao().updateVerifyStatus(user)
    }

    fun getPatientSubId():String?{
        return preferenceProvider.getSubId()
    }

    fun updateUser(user: User):Int{
        return database.getDao().updateUser(user)
    }

    fun getUserDataPref():String?{
        return preferenceProvider.getUserReqData()
    }

    suspend fun OTPVerify(key:String,tacCode:String):OTPVerifiyResponse{
        return apiRequest {
            api.verifyOTP(key,tacCode)
        }
    }

    suspend fun resendOTP(key: String):ResentOTPResponse{
        return apiRequest {
            api.resendOTP(key)
        }
    }
}