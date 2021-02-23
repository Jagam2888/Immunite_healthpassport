package com.cmg.vaccine.model.response

data class GetVaccineBlockChainResponseItem(
    val GITN: String,
    val NFCTag: String,
    val brandName: String,
    val facilityname: String,
    val gsicodeSerialCode: String,
    val itemBatch: String,
    val malNo: String,
    val manufacturerName: String,
    val manufacturerNo: String,
    val recordId: String,
    val status: String,
    val uuidTagNo: String,
    val vaccinetype: String
)