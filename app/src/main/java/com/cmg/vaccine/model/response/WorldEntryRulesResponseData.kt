package com.cmg.vaccine.model.response

data class WorldEntryRulesResponseData(
    val woenSeqNo: Int,
    val woen_country_code: String,
    val woen_duration_hours: String,
    val woen_end_date: String,
    val woen_points: String,
    val woen_rule_description: String,
    val woen_rule_match_criteria: String,
    val woen_rule_seq_no: String,
    val woen_start_date: String,
    val woen_status: String,
    val woen_test_code: String,
    val woen_vaccine_code: String
)