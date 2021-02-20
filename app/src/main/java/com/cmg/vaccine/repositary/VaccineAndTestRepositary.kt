package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine

class VaccineAndTestRepositary(
        private val database: AppDatabase
) {

    fun gettestReport(id:Int):TestReport{
        return database.getDao().getTestReport(id)
    }
}