package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.model.response.CountryResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest

class CountryListFragmentRepositary(
        private val database: AppDatabase,
        private val api:MyApi
):SafeAPIRequest() {

    suspend fun getCountriesFromAPI(): CountryResponse {
        return apiRequest {
            api.getAllCountries()
        }
    }

    fun getAllCountries():List<Countries>{
        return database.getDao().getAllCountries()
    }

    fun insertCountries(countries: Countries){
        database.getDao().insertCountries(countries)
    }
}