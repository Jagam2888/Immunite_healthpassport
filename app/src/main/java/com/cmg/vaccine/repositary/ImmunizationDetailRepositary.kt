package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.VaccineReport

class ImmunizationDetailRepositary(
    private val database: AppDatabase
) {

    fun getVaccineReport(id:Int):VaccineReport{
        return database.getDao().getVaccineReport(id)
    }
}