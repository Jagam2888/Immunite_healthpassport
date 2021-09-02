package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityChoosePastVaccinationBinding
import com.cmg.vaccine.util.navigateTo

class ChoosePastVaccinationActivity : BaseActivity() {

    private lateinit var binding:ActivityChoosePastVaccinationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_choose_past_vaccination)

        binding.btnYes.setOnClickListener {navigateTo(this,PastVaccineDetailActivity::class.java)}
    }
}