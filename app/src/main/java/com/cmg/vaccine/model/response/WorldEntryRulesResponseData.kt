package com.cmg.vaccine.model.response

data class WorldEntryRulesResponseData(
    val woenCountryCode: String,
    val woenCreatedBy: String,
    val woenCreatedDate: String,
    val woenDurationHours: String,
    val woenEnddate: String,
    val woenPoints: String,
    val woenRuleDescription: String,
    val woenRuleMatchCriteria: String,
    val woenRuleSeqNo: String,
    val woenSeqNo: Int,
    val woenStartdate: String,
    val woenStatus: String,
    val woenSubRuleFlag: Any,
    val woenTestCode: String,
    val woenUpdatedBy: String,
    val woenUpdateddate: String,
    val woenVaccineCode: String
)