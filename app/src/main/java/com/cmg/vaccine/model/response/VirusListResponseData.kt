package com.cmg.vaccine.model.response

data class VirusListResponseData(
    val virus_code: String,
    val virus_created_by: String,
    val virus_created_date: Any,
    val virus_name: String,
    val virus_seqno: Int,
    val virus_status: String,
    val virus_updated_by: String,
    val virus_updated_date: Any
)