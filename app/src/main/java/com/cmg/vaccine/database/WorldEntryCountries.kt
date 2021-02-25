package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorldEntryCountries(
    var countryName: String,
    var countryCodeAlpha: String,
    var countryMstrSeqno: Int
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
