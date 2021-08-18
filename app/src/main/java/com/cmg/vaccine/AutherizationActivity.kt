package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityAutherizationBinding
import com.cmg.vaccine.util.Passparams

class AutherizationActivity : BaseActivity() {

    private lateinit var binding:ActivityAutherizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_autherization)

        binding.btnConfirm.setOnClickListener {
            Intent(this,SuccessPopUpActivity::class.java).also {
                it.putExtra(Passparams.NAVIGATE_FROM,Passparams.AUTHERIZATION)
                startActivity(it)
            }
        }
    }
}