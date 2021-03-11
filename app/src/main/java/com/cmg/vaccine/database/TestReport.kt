package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestReport(
        /*var codeDisplay: String?,
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
        var type: String?*/
        var privateKey:String?,
        var NameDisplayTitle:String?,
        var dateDisplayTitle:String?,
        var specimenCode:String?,
        var specimenName:String?,
        var dateSampleCollected:String?,
        var timeSampleCollected:String?,
        var testCode:String?,
        var testCodeName:String?,
        var observationCode:String?,
        var observationResult:String,
        var observationDate:String?,
        var statusFinalized:String?,
        var performerName:String,
        var performerQualification:String?,
        var performerQualificationIssuerName:String?,
        var type:String?,
        var phone:String?,
        var workAddress:String?,
        var testBy:String?,
        var takenBy:String?,
        var file:String?

){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}