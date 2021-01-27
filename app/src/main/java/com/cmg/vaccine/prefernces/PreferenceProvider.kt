package com.cmg.vaccine.prefernces

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.cmg.vaccine.database.User
import com.google.gson.Gson

import io.paperdb.Paper

private const val KEY_SAVE_URL = "key_save_url"
private const val USER_DATA_REGISTER_REQ = "user_req"
private const val USER_EMAIL = "user_email"
class PreferenceProvider(
        context: Context
) {
    private val appContext = context.applicationContext

    private val prefernece : SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveURL(url:String){
        prefernece.edit().putString(
                KEY_SAVE_URL,
                url
        ).apply()
    }

    fun getURL():String?{
        return prefernece.getString(KEY_SAVE_URL,null)
    }

    fun saveEmail(email:String){
        prefernece.edit().putString(
            USER_EMAIL,
            email
        ).apply()
    }

    fun getEmail():String?{
        return prefernece.getString(USER_EMAIL,null)
    }

    fun saveUserReqData(user: User){
        val gson = Gson()
        val value:String = gson.toJson(user)
        prefernece.edit().putString(
            USER_DATA_REGISTER_REQ,
            value
        ).apply()
    }

    fun getUserReqData():String?{
        return prefernece.getString(USER_DATA_REGISTER_REQ,"")
    }
}