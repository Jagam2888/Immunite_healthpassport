package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestReport(
        var recordId:String?,
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