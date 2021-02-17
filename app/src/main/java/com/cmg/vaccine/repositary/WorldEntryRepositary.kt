package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Countries

class WorldEntryRepositary(
    private val database: AppDatabase
) {

    fun getCountries():List<Countries>{
        return database.getDao().getAllCountries()
    }
}