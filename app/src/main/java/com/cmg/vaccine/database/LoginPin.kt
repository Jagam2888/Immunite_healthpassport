package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LoginPin(
    var pin:String,
    var enable:String
){
    @PrimaryKey(autoGenerate = false)
    var id:Int = 1
}
