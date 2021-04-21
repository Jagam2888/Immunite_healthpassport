package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
        var countryCode:String?,
        var title:String?,
        var message:String?,
        var notificationGroup:String?,
        var date:String?,
        var isRead:Int
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}