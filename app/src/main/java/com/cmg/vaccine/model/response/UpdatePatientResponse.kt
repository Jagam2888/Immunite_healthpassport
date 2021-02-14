package com.cmg.vaccine.model.response

data class UpdatePatientResponse(
    val Message: String,
    val StatusCode: Int,
    val SubsId: String,
    val TimeStamp: String
)