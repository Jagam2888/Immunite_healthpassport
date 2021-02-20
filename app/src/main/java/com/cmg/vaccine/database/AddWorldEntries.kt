package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddWorldEntries(
    var countryName:String?,
    var countryIndicator:String?,
    var status:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
