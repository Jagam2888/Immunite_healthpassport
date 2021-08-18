package com.cmg.vaccine.model.response

data class GetSubscriptionPackageData(
    val subSeqNo: Int,
    val subsCountryCode: String,
    val subsCreatedBy: String,
    val subsCreatedDate: String,
    val subsCurrency: String,
    val subsEndDate: String,
    val subsPackageAmount: Double,
    val subsPackageCode: String,
    val subsPackageName: String,
    val subsPackageRemarks: String,
    val subsPackageStatus: String,
    val subsPeriod: String,
    val subsStartDate: String,
    val subsUpdatedBy: String,
    val subsUpdatedDate: String
)