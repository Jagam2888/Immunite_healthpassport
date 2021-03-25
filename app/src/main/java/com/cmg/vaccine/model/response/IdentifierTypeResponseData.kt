package com.cmg.vaccine.model.response

data class IdentifierTypeResponseData(
    val identifierCode: String,
    val identifierDisplay: String,
    val identifierSeqno: Int,
    val identifierStatus: String
)