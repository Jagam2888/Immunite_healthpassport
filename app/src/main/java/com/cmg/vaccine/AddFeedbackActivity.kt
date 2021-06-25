package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityAddFeedbackBinding

class AddFeedbackActivity : BaseActivity() {

    private lateinit var binding:ActivityAddFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_feedback)


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.radio_btn_account ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_health ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_others ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_profile ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                R.id.radio_btn_world ->{
                    if (binding.layoutRadioGroup.visibility == View.VISIBLE)
                        binding.layoutRadioGroup.visibility = View.GONE

                    if (binding.layoutExperience.visibility == View.GONE)
                        binding.layoutExperience.visibility = View.VISIBLE
                }
                else ->{
                    if (binding.layoutRadioGroup.visibility == View.GONE)
                        binding.layoutRadioGroup.visibility = View.VISIBLE

                    if (binding.layoutExperience.visibility == View.VISIBLE)
                        binding.layoutExperience.visibility = View.GONE
                }
            }
        }

        binding.imgBack.setOnClickListener {
            if (binding.layoutExperience.visibility == View.VISIBLE){
                binding.layoutExperience.visibility = View.GONE
                binding.layoutRadioGroup.visibility = View.VISIBLE
            }else{
                finish()
            }
        }

        binding.btnSubmit.setOnSingleClickListener{
            Intent(this,FeedbackSuccessActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.layoutExperience.visibility == View.VISIBLE){
            binding.layoutExperience.visibility = View.GONE
            binding.layoutRadioGroup.visibility = View.VISIBLE
        }else{
            finish()
        }
    }
}