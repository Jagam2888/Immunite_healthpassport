package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityFeedbackDetailBinding

class FeedbackDetailActivity : BaseActivity() {

    private lateinit var binding:ActivityFeedbackDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feedback_detail)

        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}