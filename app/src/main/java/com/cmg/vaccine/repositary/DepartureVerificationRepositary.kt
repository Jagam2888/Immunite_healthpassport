package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AirportCitiesName
import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.database.WorldEntryRulesByCountry
import com.cmg.vaccine.prefernces.PreferenceProvider

class DepartureVerificationRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
) {

    fun getUserData(): User {
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun getWorldEnteryRuleByCountry(countryCode:String):List<WorldEntryRulesByCountry>{
        return database.getDao().getWorldEntryRuleByCountry(countryCode)
    }

    fun getAirportCityByCode(cityCode:String): AirportCitiesName {
        return database.getDao().getAirportCityByCode(cityCode)
    }
}