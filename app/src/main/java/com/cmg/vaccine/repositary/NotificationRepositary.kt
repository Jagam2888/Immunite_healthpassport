package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Notification
import com.cmg.vaccine.model.response.UpdateFCMTokenResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class NotificationRepositary(
    private val api: MyApi,
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    suspend fun updateFCMToken(token:String): UpdateFCMTokenResponse {
        return apiRequest {
            api.updateFCMToken(preferenceProvider.getSubId()!!,token)
        }
    }

    fun insertNotification(notification: Notification){
        database.getDao().insertNotificationMessage(notification)
    }

    fun deleteNotificationByFroup(group: String){
        database.getDao().deleteNotificationByGroup(group)
    }

    fun getNotificationByGroup(group:String):List<Notification>{
        return database.getDao().getNotificationMsgByGroup(group)
    }

    fun getUnReadCount(group: String):Int{
        return database.getDao().getUnreadNotificationCount(group)
    }
    
    fun updateNotificationReadStatus(id:Int){
        database.getDao().updateNotificationReadStatus(id)
    }
}