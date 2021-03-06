package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityAdvisoryAlertBinding

class AdvisoryAlertActivity : BaseActivity() {

    private lateinit var binding:ActivityAdvisoryAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvisoryAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            finish()
        }
    }
}