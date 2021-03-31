package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestCodes(
        var wetstCountryCode: String?,
        var wetstSeqNo: Int?,
        var wetstTestCode: String?,
        var wetstObservationStatusCode: String?,
        var wetstTestcategory: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}