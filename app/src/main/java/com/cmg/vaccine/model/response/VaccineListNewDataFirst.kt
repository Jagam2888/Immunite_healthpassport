package com.cmg.vaccine.model.response

data class VaccineListNewDataFirst(
        val `data`: List<VaccineListNewDataFirstSecond>,
        val message: String,
        val reason: String,
        val statusCode: Int
)