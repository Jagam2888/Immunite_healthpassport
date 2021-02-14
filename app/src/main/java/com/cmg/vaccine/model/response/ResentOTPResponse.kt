package com.cmg.vaccine.model.response

data class ResentOTPResponse(
    val Message: String,
    val StatusCode: Int,
    val SubsId: String,
    val TimeStamp: String
)