package com.cmg.vaccine.model.response

data class GetTestCodeResponseData(
    val wetstCountryCode: String,
    val wetstSeqNo: Int,
    val wetstTestCode: String,
    val wetstTestcategory: String,
    val wetstObservationStatusCode: String
)