package com.cmg.vaccine.model.response

data class VaccineDetailListResponseData(
    val loinc: Any,
    val snomed: Any,
    val vaccineSeqno: Int,
    val vaccine_code: String,
    val vaccine_created_by: String,
    val vaccine_created_date: Any,
    val vaccine_duration_between_dosses: Any,
    val vaccine_manufacterer: String,
    val vaccine_manufacturing_year: Any,
    val vaccine_name: String,
    val vaccine_no_of_days_for_maximum_efficacy: String,
    val vaccine_no_of_doses: String,
    val vaccine_status: String,
    val vaccine_updated_by: String,
    val vaccine_updated_date: Any,
    val vaccine_virus_code: String
)