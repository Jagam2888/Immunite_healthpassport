package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VaccineReport(
    var vaccineDisplayName:String?,
    var vaccineDisplayDate:String?,
    var vaccineCode:String?,
    var vaccineLocation:String?,
    var status:String?,
    var manufactureName:String?,
    var lotNo:String?,
    var expiryDate:String?,
    var performerName:String?,
    var qualificationId:String?,
    var qualificationIssuerName:String?,
    var privateKey:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
