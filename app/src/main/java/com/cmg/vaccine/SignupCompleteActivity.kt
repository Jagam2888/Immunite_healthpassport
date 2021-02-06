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

        binding.btnSkip.setOnClickListener {
            Intent(this,MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
        }

        binding.btnCreatePin.setOnClickListener {
            Intent(this,LoginPinActivity::class.java).also {
                it.putExtra("isCreate",true)
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
        }
    }

    override fun onBackPressed() {
        Intent(this,MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
        }
    }
}