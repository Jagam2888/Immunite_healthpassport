package com.cmg.vaccine.model.request

data class SubscribeReqData(
    val currencyCode: String,
    val orderDate: String,
    val orderId: String,
    val orderStatus: String,
    val orderType: String,
    val packageAmount: String,
    val packageCode: String,
    val packageStartDate: String,
    val package_end_date: String,
    val receiptPath: String,
    val subsId: String,
    val transactionId: String
)