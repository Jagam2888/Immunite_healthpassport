package com.cmg.vaccine.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import java.util.*

abstract class LocaleHelper {

    companion object{
        const val LANGUAGE_ENGLISH:String = "en"
        const val LANGUAGE_MALAY:String = "ms"
        const val LANGUAGE_CHINESE:String = "zh"
        const val LANGUAGE_KEY:String = "language_key"


        fun setLocale(context: Context):Context{
            return updateResources(context, getLanguage(context)!!)
        }

        fun setNewLocale(context: Context,language: String):Context{
            persistLanguage(context,language)
            return updateResources(context,language)
        }

        fun getLanguage(context: Context):String?{
            val prefernece : SharedPreferences
                    = PreferenceManager.getDefaultSharedPreferences(context)
            return prefernece.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH)
        }

        fun persistLanguage(context: Context,language:String){
            val prefernece : SharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(context)
            prefernece.edit().putString(
                LANGUAGE_KEY,
                language
            ).apply()
        }

        fun updateResources(con: Context,language:String):Context{
            var context = con
            val locale = Locale(language)
            Locale.setDefault(locale)

            val resources = context.resources
            val config = Configuration(resources.configuration)

            config.setLocale(locale)
            context = context.createConfigurationContext(config)
            return context
        }
    }
}