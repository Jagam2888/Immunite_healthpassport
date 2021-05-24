package com.cmg.vaccine.model.response

data class WebCheckInResponse(
    val Message: String,
    val StatusCode: Int,
    val TimeStamp: String,
    val dobECode: String
)