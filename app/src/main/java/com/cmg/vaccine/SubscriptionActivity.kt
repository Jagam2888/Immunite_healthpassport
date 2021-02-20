package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySubscriptionBinding

class SubscriptionActivity : BaseActivity() {

    private lateinit var binding:ActivitySubscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_subscription)


        binding.radioSubscription.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_standard ->{
                    Intent(this, CheckOutActivity::class.java).also {
                        startActivity(it)
                    }
                    return@setOnCheckedChangeListener
                }
                R.id.radio_premium ->{
                    Intent(this, CheckOutActivity::class.java).also {
                        startActivity(it)
                    }
                    return@setOnCheckedChangeListener
                }
            }
        }

        binding.btnTrial.setOnClickListener {
            Intent(this, SignupCompleteActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}