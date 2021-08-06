package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AirportCitiesName (
    val airportName: String?,
    val cityCode: String?,
    val countryCode: String?,
    val countryName: String?,
    val id: Int
){
    @PrimaryKey(autoGenerate = true)
    var sno:Int = 0
}