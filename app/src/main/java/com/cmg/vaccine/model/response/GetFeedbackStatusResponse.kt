package com.cmg.vaccine.model.response

data class GetFeedbackStatusResponse(
    val attachments: List<GetFeedbackStatusResponseAttachment>,
    val `data`: List<GetFeedbackStatusResponseData>
)