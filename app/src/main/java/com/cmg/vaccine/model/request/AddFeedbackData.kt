package com.cmg.vaccine.model.request

data class AddFeedbackData(
    val caseCategory: String,
    val caseDOB: String,
    val caseDescription: String,
    val caseEncryptPK: String,
    val caseNo: String,
    val caseStatus: String,
    val caseSubId: String,
    val rating: String,
    val casePrincpleId:String,
    val caseTitle:String
)