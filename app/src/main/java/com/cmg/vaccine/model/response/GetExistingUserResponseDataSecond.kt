package com.cmg.vaccine.model.response

data class GetExistingUserResponseDataSecond(
    val countryCode: String,
    val dob: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val idType: String,
    val mobileNumber: String,
    val nationalityCountry: String,
    val placeOfBirth: String,
    val subsId: String,
    val testRecRef: String,
    val vacRecRef: String
)