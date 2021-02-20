package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseActivity() {

    private lateinit var binding:ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome)

        binding.btnNewUser.setOnClickListener {
            Intent(this,SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnExistingUser.setOnClickListener {
            Intent(this,ExistingUserActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}