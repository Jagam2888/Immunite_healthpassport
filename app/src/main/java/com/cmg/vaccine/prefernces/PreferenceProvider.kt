package com.cmg.vaccine.prefernces

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.DependentRegReq
import com.cmg.vaccine.model.request.UpdateProfileReq
import com.google.gson.Gson

import io.paperdb.Paper

private const val KEY_SAVE_URL = "key_save_url"
private const val USER_DATA_REGISTER_REQ = "user_req"
private const val EDIT_PROFILE_REQ = "edit_profile_req"
private const val ADD_DEPENDENT_PROFILE_REQ = "add_dependent_profile_req"
private const val USER_EMAIL = "user_email"
private const val USER_SUB_ID = "user_sub_id"
private const val PRINCIPAL_PRIVATE_KEY = "prinicipal_private_key"
private const val FCM_TOKEN = "fcm_token"
private const val PROFILE_IMAGE = "profile_image"

//ringtone
private const val NOTIFICATION_STATUS="notification_status"
private const val NOTIFICATION_SOUND_STATUS="notification_sound_status"
private const val RINGTONE_URL="ringtone_url"
private const val RINGTONE_NAME="ringtone_name"
private const val NEWS_UPDATE_STATUS="news_update_status"
private const val ADVISORY_STATUS="advisory_status"
private const val REGULATORY_UPDATE_STATUS="regulatory_update_status"


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

    fun saveEditProfileReq(updateProfileReq: UpdateProfileReq){
        val gson = Gson()
        val value:String = gson.toJson(updateProfileReq)
        prefernece.edit().putString(
                EDIT_PROFILE_REQ,
                value
        ).apply()
    }

    fun getEditProfileReq():String?{
        return prefernece.getString(EDIT_PROFILE_REQ,"")
    }
    fun saveaddDependentReq(dependentRegReq: DependentRegReq){
        val gson = Gson()
        val value:String = gson.toJson(dependentRegReq)
        prefernece.edit().putString(
                ADD_DEPENDENT_PROFILE_REQ,
                value
        ).apply()
    }

    fun getaddDependentReq():String?{
        return prefernece.getString(ADD_DEPENDENT_PROFILE_REQ,"")
    }
    fun saveNotificationStatus(status:Boolean){
        prefernece.edit().putBoolean(
                NOTIFICATION_STATUS,
                status
        ).apply()
    }

    fun getNotificationStatus():Boolean?{
        return prefernece.getBoolean(NOTIFICATION_STATUS,true)
    }


    fun saveNotificationSoundStatus(status:Boolean){
        prefernece.edit().putBoolean(
                NOTIFICATION_SOUND_STATUS,
                status
        ).apply()
    }

    fun getNotificationSoundStatus():Boolean?{
        return prefernece.getBoolean(NOTIFICATION_SOUND_STATUS,true)
    }

    fun saveNotificationNewsUpdatesStatus(status:Boolean){
        prefernece.edit().putBoolean(
            NEWS_UPDATE_STATUS,
            status
        ).apply()
    }

    fun getNotificationNewsUpdatesStatus():Boolean?{
        return prefernece.getBoolean(NEWS_UPDATE_STATUS,true)
    }

    fun saveNotificationAdvisoryStatus(status:Boolean){
        prefernece.edit().putBoolean(
            ADVISORY_STATUS,
            status
        ).apply()
    }

    fun getNotificationAdvisoryStatus():Boolean?{
        return prefernece.getBoolean(ADVISORY_STATUS,true)
    }

    fun saveNotificationRegulatorytatus(status:Boolean){
        prefernece.edit().putBoolean(
            REGULATORY_UPDATE_STATUS,
            status
        ).apply()
    }

    fun getNotificationRegulatoryStatus():Boolean?{
        return prefernece.getBoolean(REGULATORY_UPDATE_STATUS,true)
    }


    fun saveRingtone(ringtone_url:String,ringtone_name:String){
        prefernece.edit().putString(RINGTONE_URL, ringtone_url).apply()
        prefernece.edit().putString(RINGTONE_NAME, ringtone_name).apply()
    }

    fun getRingtoneName():String?{
        return prefernece.getString(RINGTONE_NAME,null)
    }



    fun getRingtone():String?{
        return prefernece.getString(RINGTONE_URL,null)
    }

}