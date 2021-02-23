package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestReport(
        var codeDisplay: String?,
        var codeSystem: String?,
        var collectedDateTime: String?,
        var conceptCode: String?,
        var conceptName: String?,
        var contactAddressText: String?,
        var contactAddressType: String?,
        var contactAddressUse: String?,
        var contactTelecom: String?,
        var contactTelecomValue: String?,
        var effectiveDateTime: String?,
        var name: String?,
        var qualificationIssuerName: String?,
        var qualitificationIdentifier: String?,
        var recordId: String?,
        var status: String?,
        var type: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}