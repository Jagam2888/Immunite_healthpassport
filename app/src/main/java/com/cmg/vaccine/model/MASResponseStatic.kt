package com.cmg.vaccine.model

data class MASResponseStatic(
    val agency: String,
    val `data`: MASResponseStaticData,
    val purpose: String,
    val status: String
)