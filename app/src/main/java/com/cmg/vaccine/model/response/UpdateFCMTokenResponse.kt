package com.cmg.vaccine.model.response

data class UpdateFCMTokenResponse(
    val Message: String,
    val PrivateKey: String,
    val StatusCode: Int,
    val TimeStamp: String
)