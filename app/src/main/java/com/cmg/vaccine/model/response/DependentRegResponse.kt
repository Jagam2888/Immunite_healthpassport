package com.cmg.vaccine.model.response

data class DependentRegResponse(
    val DependentPrivateKey: String,
    val Message: String,
    val StatusCode: Int,
    val TimeStamp: String
)