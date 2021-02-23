package com.cmg.vaccine.model.response

data class Data(
    val `data`: List<DataX>,
    val message: String,
    val reason: String,
    val statusCode: Int
)