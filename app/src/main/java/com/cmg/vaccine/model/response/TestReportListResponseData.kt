package com.cmg.vaccine.model.response

data class TestReportListResponseData(
    val createdBy: String?,
    val filePath: String?,
    val observationCodeSnomedCt: String?,
    val observationDateTime: String?,
    val observationResult: String?,
    val performerAddTxt: String?,
    val performerAddType: String?,
    val performerAddUse: String?,
    val performerContactTelecom: String?,
    val performerContactTelecomValue: String?,
    val performerName: String?,
    val performerQualificationIdentifier: String?,
    val performerQualificationIssuerName: String?,
    val performerType: String?,
    val specimenCode: String?,
    val specimenDateSampleCollected: String?,
    val specimenName: String?,
    val status: String?,
    val subsId: String?,
    val testCode: String?,
    val testSeqno: String?,
    val updatedBy: String?
)