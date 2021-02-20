package com.cmg.vaccine.model.response

data class TestTypeResponseData(
    val loinc: Any,
    val snomed: Any,
    val testTypeSeqno: Int,
    val test_code: String,
    val test_created_by: String,
    val test_created_date: Any,
    val test_name: String,
    val test_result_validaty: String,
    val test_specimen: String,
    val test_status: String,
    val test_update_date: Any,
    val test_updated_by: String,
    val test_virus_code: String
)