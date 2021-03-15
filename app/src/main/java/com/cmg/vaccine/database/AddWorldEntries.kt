package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddWorldEntries(
    var countryName:String?,
    var countryCodeAlpha:String?,
    var countryIndicator:String?,
    var status:String?,
    var order:Int=0
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
