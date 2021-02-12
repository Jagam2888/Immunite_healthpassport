package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityVaccineAndTestReportBinding

class VaccineAndTestReportActivity : AppCompatActivity() {

    private lateinit var binding:ActivityVaccineAndTestReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_vaccine_and_test_report)

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}