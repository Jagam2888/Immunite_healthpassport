package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vaccine(
        var GITN: String?,
        var NFCTag: String?,
        var brandName: String?,
        var facilityname: String?,
        var gsicodeSerialCode: String?,
        var itemBatch: String?,
        var malNo: String?,
        var manufacturerName: String?,
        var manufacturerNo: String?,
        var recordId: String?,
        var status: String?,
        var uuidTagNo: String?,
        var vaccinetype: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}