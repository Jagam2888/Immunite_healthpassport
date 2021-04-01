package com.cmg.vaccine.model

data class JoinWorldEntryRuleAndPriority(
        val prioRuleCountry: String?,
        val prioRuleCriteria: String?,
        val prioRuleNo: String?,
        val prioRulePair: String?,
        val woen_country_code: String?,
        val woen_duration_hours: String?,
        val woen_rule_match_criteria: String?,
        val woen_rule_seq_no: String?,
        val woen_test_code: String?
)
