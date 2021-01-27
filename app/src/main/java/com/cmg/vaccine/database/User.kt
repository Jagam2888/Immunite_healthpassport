package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var countryCode: String,
    var createdBy: Int,
    var createdDate: Long,
    var email: String,
    var backupEmail: String,
    var firstName: String,
    var gender: String,
    var lastName: String,
    var mobileNumber: String,
    var patientIdNo: String,
    var patientIdType: String,
    var patientSeqno: Int,
    var privateKey: String,
    var password: String,
    var updatedBy: Int,
    var updatedDate: Long
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
