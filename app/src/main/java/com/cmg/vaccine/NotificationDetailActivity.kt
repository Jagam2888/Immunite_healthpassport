package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityNotificationDetailBinding

class NotificationDetailActivity : BaseActivity() {
    private lateinit var binding:ActivityNotificationDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification_detail)

        val msg = intent.extras?.get("msg")
        binding.txtMsg.text = msg.toString()

        val title = intent.extras?.get("title")
        binding.txtTitle.text = title.toString()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}