package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySuccessPopBinding

class SuccessPopUpActivity : BaseActivity() {

    private lateinit var binding:ActivitySuccessPopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_success_pop)

        binding.btnDone.setOnClickListener {
            finish()
        }
    }
}