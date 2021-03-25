package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.*
import com.cmg.vaccine.prefernces.PreferenceProvider

class DepartureVerificationRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
) {

    fun getUserData(): User {
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun getWorldEnteryRuleByCountry(countryCode:String):List<WorldEntryRulesByCountry>{
        return database.getDao().getWorldEntryRuleByCountryByCode(countryCode)
    }

    fun getAirportCityByCode(cityCode:String): AirportCitiesName {
        return database.getDao().getAirportCityByCode(cityCode)
    }

    fun getTestReportList(privateKey:String):List<TestReport>{
        return database.getDao().getTestReportList(privateKey)
    }
}