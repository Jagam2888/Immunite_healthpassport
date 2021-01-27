package com.cmg.vaccine.model.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val Message: String,
    @SerializedName("private key")
    val privatekey: String
)