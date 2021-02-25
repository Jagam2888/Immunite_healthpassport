package com.cmg.vaccine.model.response

data class TestReportListBlockChainDataFirst(
        val `data`: List<TestReportListBlockChainDataSecond>,
        val message: String,
        val reason: String,
        val statusCode: Int
)