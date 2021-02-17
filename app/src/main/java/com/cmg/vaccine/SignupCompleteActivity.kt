package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySignupCompleteBinding
import com.cmg.vaccine.util.Passparams

class SignupCompleteActivity : BaseActivity() {
    private lateinit var binding:ActivitySignupCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup_complete)

        binding.btnSkip.setOnClickListener {
            Intent(this,MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }

        binding.btnCreatePin.setOnClickListener {
            Intent(this,LoginPinActivity::class.java).also {
                it.putExtra(Passparams.ISCREATE,"create")
                //it.putExtra(Passparams.ISSETTINGS,false)
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
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