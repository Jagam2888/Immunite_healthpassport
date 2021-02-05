package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityChangeLanguageBinding
import com.cmg.vaccine.util.LocaleHelper

class ChangeLanguageActivity : BaseActivity() {
    private lateinit var binding:ActivityChangeLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_language)

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnConfirm.setOnClickListener {
            if (binding.spinnerLanguage.selectedItemPosition == 0){
                LocaleHelper.setNewLocale(this,LocaleHelper.LANGUAGE_ENGLISH)
            }else if(binding.spinnerLanguage.selectedItemPosition == 1){
                LocaleHelper.setNewLocale(this,LocaleHelper.LANGUAGE_MALAY)
            }else if(binding.spinnerLanguage.selectedItemPosition == 2){
                LocaleHelper.setNewLocale(this,LocaleHelper.LANGUAGE_CHINESE)
            }

            Intent(this,LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(it)
            }
        }
    }
}