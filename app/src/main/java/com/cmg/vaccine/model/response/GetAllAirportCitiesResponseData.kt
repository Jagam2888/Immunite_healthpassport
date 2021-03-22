package com.cmg.vaccine.model.response

data class GetAllAirportCitiesResponseData(
    val airportName: String,
    val cityCode: String,
    val countryCode: String,
    val countryName: String,
    val id: Int
)