package com.cmg.vaccine.model.response

data class VaccineListBlockChainDataFirst(
        val `data`: List<VaccineListBlockChainDataSecond>,
        val message: String,
        val reason: String,
        val statusCode: Int
)