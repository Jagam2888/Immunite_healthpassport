package com.cmg.vaccine.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var fullName: String,
    var email: String,
    var backupEmail: String?,
    var mobileNumber: String,
    var passportNumber:String?,
    var patientIdType:String?,
    var patientIdNo:String?,
    var countryCode: String,
    var placeBirth: String,
    var gender: String,
    var nationality:String,
    var dob:String?,
    var dobTime:String?,
    var address:String?,
    var city:String?,
    var state:String?,
    var privateKey: String?,
    var parentSubscriberId: String?,
    var virifyStatus:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
