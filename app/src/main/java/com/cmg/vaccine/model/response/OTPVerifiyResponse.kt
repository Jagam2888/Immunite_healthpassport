package com.cmg.vaccine.model.response

data class OTPVerifiyResponse(
    val message: String,
    val privateKey: String,
    val success: Boolean
)