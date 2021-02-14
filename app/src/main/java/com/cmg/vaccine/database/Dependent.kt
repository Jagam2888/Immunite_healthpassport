package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dependent(
        var countryCode: String?,
        var dob: String?,
        var dobTime: String?,
        var email: String?,
        var firstName: String?,
        var gender: String?,
        var idNo: String?,
        var idType: String?,
        var masterSubsId: String?,
        var subsId: String?,
        var mobileNumber: String?,
        var nationalityCountry: String?,
        var passportNo: String?,
        var placeOfBirth: String?,
        var relationship: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
