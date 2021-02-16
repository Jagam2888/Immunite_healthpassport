package com.cmg.vaccine.model.response

data class DependentRegResponse(
    val SubsId: String,
    val privateKey: String,
    val Message: String,
    val StatusCode: Int,
    val TimeStamp: String
)