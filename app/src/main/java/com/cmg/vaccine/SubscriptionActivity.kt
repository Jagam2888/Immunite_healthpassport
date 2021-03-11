package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySubscriptionBinding

class SubscriptionActivity : BaseActivity() {

    private lateinit var binding:ActivitySubscriptionBinding
    var isFreeTrial = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_subscription)


        binding.radioSubscription.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_standard ->{
                    /*Intent(this, CheckOutActivity::class.java).also {
                        startActivity(it)
                    }*/
                    isFreeTrial = false
                    binding.btnTrial.text = resources.getString(R.string.subscribe)
                    return@setOnCheckedChangeListener
                }
                R.id.radio_premium ->{
                    isFreeTrial = false
                    binding.btnTrial.text = resources.getString(R.string.subscribe)
                    /*Intent(this, CheckOutActivity::class.java).also {
                        startActivity(it)
                    }*/
                    return@setOnCheckedChangeListener
                }
                R.id.radio_free ->{
                    isFreeTrial = true
                    binding.btnTrial.text = resources.getString(R.string.get_1_month_free_trail)
                }
            }
        }

        binding.btnTrial.setOnClickListener {
            if (isFreeTrial) {
                Intent(this, SignupCompleteActivity::class.java).also {
                    startActivity(it)
                }
            }else{
                Intent(this, CheckOutActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
}