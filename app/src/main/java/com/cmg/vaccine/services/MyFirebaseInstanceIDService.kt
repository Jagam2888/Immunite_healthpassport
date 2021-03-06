package com.cmg.vaccine.services

import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.Passparams
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.paperdb.Paper
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class MyFirebaseInstanceIDService():FirebaseMessagingService() {
    //val prefernece:PreferenceProvider = PreferenceProvider(this)
    //val api = MyApi(applicationContext)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notificationService = NotificationService(applicationContext)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("remote_data",remoteMessage.data.toString())
            try {
                val message = remoteMessage.data["message"]
                val title = remoteMessage.data["title"]
                Log.d("remote_data",message.toString())
                notificationService.createNotificationChannel(message!!,title!!)
            }catch (e:JSONException){
                e.printStackTrace()
            }

        }
        /*if (remoteMessage.notification != null){
            remoteMessage.notification?.body?.let { notificationService.createNotificationChannel(it) }
        }*/
    }

    override fun onNewToken(token: String) {

        if (token != null) {
            Paper.book().write(Passparams.FCM_TOKEN,token)
        }
        Log.d("Firebase", "Refreshed token: $token")
    }

    private fun updateToken(token: String){
        Couritnes.main {
            try {

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

   /* fun saveFCMToken(token:String){
        prefernece.edit().putString(
            Passparams.FCM_TOKEN,
            token
        ).apply()
    }*/
}