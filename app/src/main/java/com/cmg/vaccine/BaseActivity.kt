package com.cmg.vaccine

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.cmg.vaccine.util.LocaleHelper

open class BaseActivity:AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase!!))
    }
}