package com.cmg.vaccine.model.response

data class TestReportListBlockChainDataSecond(
    val codeDisplay: String,
    val codeSystem: String,
    val collectedDateTime: String,
    val conceptCode: String,
    val conceptName: String,
    val contactAddressText: String,
    val contactAddressType: String,
    val contactAddressUse: String,
    val contactTelecom: String,
    val contactTelecomValue: String,
    val effectiveDateTime: String,
    val name: String,
    val qualificationIssuerName: String,
    val qualitificationIdentifier: String,
    val recordId: String,
    val status: String,
    val type: String
)