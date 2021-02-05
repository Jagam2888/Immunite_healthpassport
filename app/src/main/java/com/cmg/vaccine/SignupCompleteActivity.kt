package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySignupCompleteBinding

class SignupCompleteActivity : BaseActivity() {
    private lateinit var binding:ActivitySignupCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup_complete)

        binding.btnLogin.setOnClickListener {
            Intent(this,LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
        }
    }

    override fun onBackPressed() {
        Intent(this,LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
        }
    }
}