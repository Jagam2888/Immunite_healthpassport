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
private const val USER_SUB_ID = "user_sub_id"
private const val PRINCIPAL_PRIVATE_KEY = "prinicipal_private_key"
private const val FCM_TOKEN = "fcm_token"
private const val PROFILE_IMAGE = "profile_image"
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

    fun saveFCMToken(token:String){
        prefernece.edit().putString(
            FCM_TOKEN,
            token
        ).apply()
    }

    fun getFCMTOKEN():String?{
        return prefernece.getString(FCM_TOKEN,null)
    }

    fun saveSubId(subId:String){
        prefernece.edit().putString(
                USER_SUB_ID,
                subId
        ).apply()
    }

    fun getSubId():String?{
        return prefernece.getString(USER_SUB_ID,null)
    }

    fun saveProfileImage(img:String){
        prefernece.edit().putString(
                PROFILE_IMAGE,
                img
        ).apply()
    }

    fun getProfileImage():String?{
        return prefernece.getString(PROFILE_IMAGE,null)
    }

    fun savePrincipalPrivateKey(privateKey:String){
        prefernece.edit().putString(
            PRINCIPAL_PRIVATE_KEY,
            privateKey
        ).apply()
    }

    fun getPrincipalPrivateKey():String?{
        return prefernece.getString(PRINCIPAL_PRIVATE_KEY,null)
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