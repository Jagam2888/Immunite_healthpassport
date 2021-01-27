package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityPastVaccineDetailBinding

class PastVaccineDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPastVaccineDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_past_vaccine_detail)

        binding.btnSubmit.setOnClickListener {
            Intent(this,AutherizationActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}