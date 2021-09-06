package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider

/**
 * Created by jagad on 9/6/2021
 */
class FaceDetectRepositary(
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
) {

    fun user():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }
}