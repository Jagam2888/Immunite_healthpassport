package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.User
import com.cmg.vaccine.prefernces.PreferenceProvider


class ChangePasswordRepositary(
        private val database: AppDatabase,
        private val preferenceProvider: PreferenceProvider
) {

    fun getUser(): User {
        return database.getDao().getUserData(preferenceProvider.getEmail()!!)
    }

    suspend fun updatePassword(user: User):Int{
        return database.getDao().updatePassword(user)
    }
}