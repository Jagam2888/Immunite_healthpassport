package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dependent(
    var countryCode: String?,
    var dob: String?,
    var email: String,
    var firstName: String?,
    var gender: String?,
    var idNo: String?,
    var parentPrivateKey: String,
    var childPrivateKey: String,
    var mobileNumber: String?,
    var nationalityCountry: String?,
    var passportNo: String?,
    var provinceState: String?,
    var relationship: String?,
    var residentialAddress: String?,
    var townCity: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
