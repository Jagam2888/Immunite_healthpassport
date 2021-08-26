package com.cmg.vaccine.model.response

import com.google.gson.annotations.SerializedName

data class GetFeedbackStatusResponse(
    val attachments: List<GetFeedbackStatusResponseAttachment>,
    val `data`: List<GetFeedbackStatusResponseData>,
    @SerializedName("feedback_chronology")
    val chronology: List<GetFeedbackChronology>
)