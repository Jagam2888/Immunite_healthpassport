package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityCheckOutBinding

class CheckOutActivity : BaseActivity() {

    private lateinit var binding:ActivityCheckOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_check_out)

        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}