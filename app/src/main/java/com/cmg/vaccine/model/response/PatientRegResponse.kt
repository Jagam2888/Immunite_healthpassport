package com.cmg.vaccine.model.response

data class PatientRegResponse(
    val Message: String,
    val ParentSubscriberId: String,
    val StatusCode: Int,
    val TimeStamp: String
)