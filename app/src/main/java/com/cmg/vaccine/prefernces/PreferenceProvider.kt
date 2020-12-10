package com.cmg.vaccine.prefernces

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

import io.paperdb.Paper

private const val KEY_SAVE_URL = "key_save_url"
class PreferenceProvider(
        context: Context
) {
    private val appContext = context.applicationContext

    private val prefernece : SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveURL(url:String){
        prefernece.edit().putString(
                KEY_SAVE_URL,
                url
        ).apply()
    }

    fun getURL():String?{
        return prefernece.getString(KEY_SAVE_URL,null)
    }
}