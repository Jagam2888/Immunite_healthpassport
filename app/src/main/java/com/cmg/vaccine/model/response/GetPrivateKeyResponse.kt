package com.cmg.vaccine.model.response

import com.google.gson.annotations.SerializedName

data class GetPrivateKeyResponse(
    val Message: String,
    @SerializedName("Private Key")
    val PrivateKey: String,
    val StatusCode: Int,
    val TimeStamp: String
)