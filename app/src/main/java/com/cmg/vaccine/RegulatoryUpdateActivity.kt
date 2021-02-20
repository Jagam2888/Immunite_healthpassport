package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityRegulatoryUpdateBinding

class RegulatoryUpdateActivity : BaseActivity() {

    private lateinit var binding:ActivityRegulatoryUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_regulatory_update)

        binding.imageView.setOnClickListener {
            finish()
        }
    }
}