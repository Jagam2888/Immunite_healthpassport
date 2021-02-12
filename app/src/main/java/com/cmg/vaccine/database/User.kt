package com.cmg.vaccine.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var patientSeqno: Int,
    var fullName: String,
    var lastName: String?,
    var email: String,
    var backupEmail: String?,
    var mobileNumber: String,
    var passportNumber:String,
    var patientIdNo: String?,
    var countryCode: String,
    var placeBirth: String,
    var gender: String,
    var nationality:String,
    var dob:String,
    var address:String?,
    var city:String?,
    var state:String?,
    var password: String?,
    var createdBy: Int?,
    var createdDate: Long?,
    var updatedBy: Int?,
    var updatedDate: Long?,
    var privateKey: String,
    var virifyStatus:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
