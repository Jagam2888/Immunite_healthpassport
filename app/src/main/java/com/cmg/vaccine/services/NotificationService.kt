package com.cmg.vaccine.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.NewsUpdateActivity
import com.cmg.vaccine.NotificationDetailActivity
import com.cmg.vaccine.NotificationGroupListActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Notification
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.repositary.NotificationRepositary
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.viewmodel.HomeViewModel
import java.lang.Exception


class NotificationService(
        private val context: Context
) {

    private val CHANNEL_ID = "Immunitee_channel"

    private val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    var pendingIntent:PendingIntent?=null

    val database = AppDatabase(context)
    var notificationMessage:String?=null

    var group = "N"

    fun createNotificationChannel(msg:String,title:String){
        try{
            notificationMessage = msg
            val msgArray = msg.split("|")
            if (msgArray.size > 3){
                notificationMessage = msgArray[0]
                group = msgArray[2]

                val notification = Notification(
                    msgArray[1],
                        title,
                    msgArray[0],
                    msgArray[2],
                    msgArray[3],
                    0
                )
                database.getDao().insertNotificationMessage(notification)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.resources.getString(R.string.channel_name)
            val descriptionText = context.resources.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system

            notificationManager.createNotificationChannel(channel)
        }

        Intent(context,NewsUpdateActivity::class.java).also {
            it.putExtra(Passparams.NOTIFICATION_FROM, group)
            pendingIntent = PendingIntent.getActivity(context,0,it,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        var pref= PreferenceProvider(context)
        if (!pref.getRingtone().isNullOrEmpty()) {
            var currentRingtoneUrl = Uri.parse(pref.getRingtone())
            var ringtone = RingtoneManager.getRingtone(this.context, currentRingtoneUrl)
            if(pref.getNotificationSoundStatus()!!){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ringtone.audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
                } else {
                    // ringtone.streamType = AudioManager.STREAM_ALARM
                }
                ringtone.play()
            }
        }


        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent).setAutoCancel(true)

        if (pref.getNotificationSoundStatus() == false) {
            with(NotificationManagerCompat.from(context)) {
                cancelAll()
            }
        }else {
            val notificationId = System.currentTimeMillis().toInt()
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        }



    }
}