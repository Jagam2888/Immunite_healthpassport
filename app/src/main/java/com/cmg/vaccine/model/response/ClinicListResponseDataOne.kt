package com.cmg.vaccine.model.response

data class ClinicListResponseDataOne(
        val `data`: List<ClinicListResponseDataTwo>,
        val itemsPerPage: Int,
        val pageIndex: Int,
        val totalItems: Int,
        val totalPages: Int
)