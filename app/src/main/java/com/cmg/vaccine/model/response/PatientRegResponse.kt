package com.cmg.vaccine.model.response

data class PatientRegResponse(
    val Message: String,
    val ParentPrivateKey: String,
    val StatusCode: Int,
    val TimeStamp: String
)