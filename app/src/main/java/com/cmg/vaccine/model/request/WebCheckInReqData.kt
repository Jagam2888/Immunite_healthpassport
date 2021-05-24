package com.cmg.vaccine.model.request

data class WebCheckInReqData(
    val grantDobECode: String,
    val grantImmuniteeDob: String,
    val grantImmuniteeIdNo: String,
    val grantImmuniteeIdType: String,
    val grantImmuniteeName: String,
    val grantImmuniteePassportNo: String,
    val grantImuniteeExpiry: String,
    val grantPk: String,
    val subsId: String
)