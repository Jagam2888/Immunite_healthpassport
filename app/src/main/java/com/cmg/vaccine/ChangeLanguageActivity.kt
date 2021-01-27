package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityChangeLanguageBinding

class ChangeLanguageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChangeLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_language)

        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}