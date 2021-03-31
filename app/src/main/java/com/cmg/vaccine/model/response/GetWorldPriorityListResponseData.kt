package com.cmg.vaccine.model.response

data class GetWorldPriorityListResponseData(
    val prioRuleCountry: String,
    val prioRuleCriteria: String,
    val prioRuleNo: String,
    val prioSeqNo: Int,
    val prioRulePair:String
)