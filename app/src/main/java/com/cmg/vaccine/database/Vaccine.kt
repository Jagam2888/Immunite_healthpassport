package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vaccine(
        var brandName: String?,
        var facilityName: String?,
        var gitn: String?,
        var gsicodeSerialCode: String?,
        var itemBatch: String?,
        var item_expiry: String?,
        var malNo: String?,
        var manufacturerName: String?,
        var manufacturerNo: String?,
        var nfcTag: String?,
        var patientSeqNo: Int?,
        var privateKey: String?,
        var uuidTagNo: String?,
        var vaccinationStatus: String?,
        var vaccineDate: String?,
        var vaccineType: String?,
        var vccprivatekey: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}