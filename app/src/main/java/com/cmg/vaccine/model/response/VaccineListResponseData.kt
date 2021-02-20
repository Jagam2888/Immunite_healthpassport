package com.cmg.vaccine.model.response

data class VaccineListResponseData(
        val brandName: String?,
        val facilityName: String?,
        val gitn: String?,
        val gsicodeSerialCode: String?,
        val itemBatch: String?,
        val item_expiry: String?,
        val malNo: String?,
        val manufacturerName: String?,
        val manufacturerNo: String?,
        val nfcTag: String?,
        val patientSeqNo: Int?,
        val privateKey: String?,
        val uuidTagNo: String?,
        val vaccinationStatus: String?,
        val vaccineDate: String?,
        val vaccineType: String?,
        val vccprivatekey: String?
)