package com.cmg.vaccine.model.request

data class Data(
    val countryCode: String,
    val dob: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val idNo: String,
    val idType: String,
    val mobileNumber: String,
    val nationalityCountry: String,
    val passportNo: String,
    val placeOfBirth: String
)