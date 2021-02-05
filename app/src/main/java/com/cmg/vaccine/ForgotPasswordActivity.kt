package com.cmg.vaccine

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding:ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)

        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}