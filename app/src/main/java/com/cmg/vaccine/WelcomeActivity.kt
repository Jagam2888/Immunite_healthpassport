package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityWelcomeBinding
import com.cmg.vaccine.fragment.WelcomeDialogFragment
import immuniteeEncryption.EncryptionUtils

class WelcomeActivity : BaseActivity() {

    private lateinit var binding:ActivityWelcomeBinding
    var lastClickSignup:Long = 0
    var lastClickExisting:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome)

        WelcomeDialogFragment().show(supportFragmentManager,"Welcome")

        binding.btnNewUser.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickSignup<1000){
                return@setOnClickListener
            }
            lastClickSignup = SystemClock.elapsedRealtime()
            Intent(this,SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnExistingUser.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickExisting<1000){
                return@setOnClickListener
            }
            lastClickExisting = SystemClock.elapsedRealtime()
            Intent(this,ExistingUserActivity::class.java).also {
                startActivity(it)
            }
        }



    }


}