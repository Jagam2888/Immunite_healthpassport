package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.response.CountryResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider

class SignUpRepositary(
    private val api: MyApi,
    private val database:AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {


    fun saveUser(user: User){
        preferenceProvider.saveUserReqData(user)
    }

    suspend fun getAllCountries():CountryResponse{
        return apiRequest {
            api.getAllCountries()
        }
    }

    fun getAllCountriesDB():List<Countries>{
        return database.getDao().getAllCountries()
    }

    fun insertCountries(countries: Countries){
        database.getDao().insertCountries(countries)
    }
}