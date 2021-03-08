package com.cmg.vaccine

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySuccessAccountRestoredBinding
import com.cmg.vaccine.util.Passparams

class SuccessAccountRestoredActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySuccessAccountRestoredBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_success_account_restored)

        initViews()
    }

    private fun initViews() {

        binding.btnLoginPin.setOnClickListener {
            Intent(this,LoginPinActivity::class.java).also {
                it.putExtra(Passparams.ISCREATE,"create")
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                finish()
            }
        }

        binding.txtSkip.setOnClickListener {
            Intent(this,MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
        }
    }
}