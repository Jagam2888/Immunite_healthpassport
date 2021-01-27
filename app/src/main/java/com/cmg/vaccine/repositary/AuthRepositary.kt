package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class AuthRepositary(
    private val database:AppDatabase,
    private val preferenceProvider: PreferenceProvider
) : SafeAPIRequest(){

    /*suspend fun loginUser(authReq : AuthRequest) : AuthResponse{
        return apiRequest {
            api.loginUser(authReq)
        }
    }*/

    fun saveUserEmail(email:String){
        preferenceProvider.saveEmail(email)
    }

    fun getEmail():String?{
        return preferenceProvider.getEmail()
    }

    fun getLogin(email:String,password:String):User?{
        return database.getDao().login(email,password)
    }
}