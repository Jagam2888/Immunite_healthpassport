package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityWelcomeBinding
import immuniteeEncryption.EncryptionUtils

class WelcomeActivity : BaseActivity() {

    private lateinit var binding:ActivityWelcomeBinding
    var lastClickSignup:Long = 0
    var lastClickExisting:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome)

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

        Log.d("decrypt",decryptKey("PFOdgH8XhsOMbROOpjgUADEWqchszqq7dvUIFLkYfen2aPuYIOut7FHin2bL+rOavZG/QydEQotq trP+KcPa0+9565jrKYp6CMp0wOp+zGw=")!!)


    }

    fun decryptKey(key:String):String?{
        return EncryptionUtils.decryptBackupKey(key,"2015-09-14")
    }
}