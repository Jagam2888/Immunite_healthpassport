package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*

class WorldEntryRepositary(
    private val database: AppDatabase
) {

    fun getCountries():List<Countries>{
        return database.getDao().getAllCountries()
    }

    fun insertWorldEntry(addWorldEntries: AddWorldEntries){
        database.getDao().insertAddWorldEntry(addWorldEntries)
    }

    fun getWorldEntriesList():List<AddWorldEntries>{
        return database.getDao().getWorldEntries()
    }

    fun deleteCountry(countryName: String){
        database.getDao().deleteAddWorldEntries(countryName)
    }

    fun countryExists(countryName:String):Int{
        return database.getDao().getCountryExists(countryName)
    }

    fun getVaccineList():List<Vaccine>{
        return database.getDao().getVaccineList()
    }

    fun getTestReportList():List<TestReport>{
        return database.getDao().getTestReportList()
    }
}