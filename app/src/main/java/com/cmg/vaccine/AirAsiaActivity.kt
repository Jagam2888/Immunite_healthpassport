package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityAirAsiaBinding

class AirAsiaActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAirAsiaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_air_asia)

        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}