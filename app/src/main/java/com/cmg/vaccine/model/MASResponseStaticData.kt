package com.cmg.vaccine.model

data class MASResponseStaticData(
    val reqAirline: String,
    val reqArrivalDestination: String,
    val reqDepatureDestination: String,
    val reqEtaTime: String,
    val reqEtdTime: String,
    val reqFlightNo: String,
    val reqStaffName: String
)