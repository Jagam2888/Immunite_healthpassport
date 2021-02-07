package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.cmg.vaccine.model.response.DependentRegResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class DependentRepositary(
    private val api: MyApi,
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    suspend fun dependentSignUp(dependentRegReq: DependentRegReq):DependentRegResponse{
        return apiRequest {
            api.dependentSignUp(dependentRegReq)
        }
    }

    fun getUserData():User{
        return database.getDao().getUserData(preferenceProvider.getEmail()!!,"Y")
    }

    suspend fun insertDependentSignUp(dependent: Dependent):Long{
        return database.getDao().insertDependent(dependent)
    }

    fun getDependent(privateKey:String):Dependent{
        return database.getDao().getDependent(privateKey)
    }

    fun updateDependent(dependent: Dependent):Int{
        return database.getDao().updateDependent(dependent)
    }

    suspend fun updateDependentProfile(updateProfileReq: UpdateProfileReq):DependentRegResponse{
        return apiRequest {
            api.updateDependentProfile(updateProfileReq)
        }
    }
}