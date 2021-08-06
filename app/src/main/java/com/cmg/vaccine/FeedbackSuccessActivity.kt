package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityFeedbackSuccessBinding

class FeedbackSuccessActivity : BaseActivity() {

    private lateinit var binding:ActivityFeedbackSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feedback_success)

        binding.btnDone.setOnSingleClickListener{
            finish()
        }
    }
}