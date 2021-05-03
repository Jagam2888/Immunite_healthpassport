package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScanAirportEntry(
        var agency:String?,
        var purpose:String?,
        var flightNo:String?,
        var etdTime:String,
        var etaTime:String?,
        var depatureDestination:String?,
        var arrivalDestination:String?,
        var staffName:String?,
        var airLines:String?,
        var status:String?,
        var eCode:String?,
        var dobEcode:String?,
        var scanDateTime:String?,
        var remarks:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}