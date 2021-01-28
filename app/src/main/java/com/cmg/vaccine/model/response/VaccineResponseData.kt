package com.cmg.vaccine.model.response

data class VaccineResponseData(
    val brandName: String,
    val facilityName: String,
    val gitn: String,
    val gsicodeSerialCode: String,
    val itemBatch: String,
    val item_expiry: String,
    val malNo: String,
    val manufacturerName: String,
    val manufacturerNo: String,
    val nfcTag: String,
    val privateKey: String,
    val uuidTagNo: String,
    val vaccinationStatus: String,
    val vaccineDate: String,
    val vaccineSeqno: Int,
    val vaccineType: String
)