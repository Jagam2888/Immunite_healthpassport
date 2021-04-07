package com.cmg.vaccine.model.response

data class BlockChainErrorCodeResponseData(
    val prioRuleCountry: String,
    val prioRuleCriteria: String,
    val prioRuleNo: String,
    val prioSeqNo: Int
)