package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityAutherizationBinding

class AutherizationActivity : BaseActivity() {

    private lateinit var binding:ActivityAutherizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_autherization)

        binding.btnConfirm.setOnClickListener {
            Intent(this,SuccessPopUpActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}