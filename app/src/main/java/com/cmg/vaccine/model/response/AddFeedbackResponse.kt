package com.cmg.vaccine.model.response

data class AddFeedbackResponse(
    val Message: String,
    val StatusCode:Int,
    val CaseNo:String,
    val TimeStamp:String
)