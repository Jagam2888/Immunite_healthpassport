package com.cmg.vaccine.services

import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.util.Passparams
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.paperdb.Paper

class MyFirebaseInstanceIDService(
):FirebaseMessagingService() {
    //val prefernece:PreferenceProvider = PreferenceProvider(this)
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }

    override fun onNewToken(token: String) {

        if (token != null) {
            Paper.book().write(Passparams.FCM_TOKEN,token)
        }
        Log.d("Firebase", "Refreshed token: $token")
    }

   /* fun saveFCMToken(token:String){
        prefernece.edit().putString(
            Passparams.FCM_TOKEN,
            token
        ).apply()
    }*/
}