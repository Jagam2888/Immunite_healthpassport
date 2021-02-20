package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestReport(
        var filePath: String?,
        var observationCodeSnomedCt: String?,
        var observationDateTime: String?,
        var observationResult: String?,
        var performerAddTxt: String?,
        var performerAddType: String?,
        var performerAddUse: String?,
        var performerContactTelecom: String?,
        var performerContactTelecomvarue: String?,
        var performerName: String?,
        var performerQualificationIdentifier: String?,
        var performerQualificationIssuerName: String?,
        var performerType: String?,
        var specimenCode: String?,
        var specimenDateSampleCollected: String?,
        var specimenName: String?,
        var status: String?,
        var subsId: String?,
        var testCode: String?,
        var testSeqno: String?,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}