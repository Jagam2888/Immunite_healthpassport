package com.cmg.vaccine.model.response

data class ClinicListResponseDataOne(
        val address: String,
        val address2: Any,
        val city: String,
        val companyRoc: String,
        val contactPerson: String,
        val contactPersonEmail: String,
        val contactPersonPhone: String,
        val country: String,
        val expDate: Long,
        val facilityCode: String,
        val facilityName: String,
        val facilityPrefix: String,
        val facilitySeqno: Int,
        val facilityType: String,
        val incharge: String,
        val officeEmail: String,
        val officePhone: String,
        val outletId: String,
        val postcode: String,
        val province: String,
        val state: String,
        val subscriptionSeqno: Int,
        val waitRoomCapacity: Int
)