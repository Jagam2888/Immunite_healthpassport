package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AirportCitiesName (
    val airportName: String?=null,
    val cityCode: String?=null,
    val countryCode: String?=null,
    val countryName: String?=null,
    val id: Int?=null
){
    @PrimaryKey(autoGenerate = true)
    var sno:Int = 0
}