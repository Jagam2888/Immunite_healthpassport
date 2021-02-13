package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityNotificationGroupListBinding

class NotificationGroupListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNotificationGroupListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification_group_list)

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.layoutNewsUpdate.setOnClickListener {
            Intent(this,NewsUpdateActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.layoutAdvisoryAlert.setOnClickListener {
            Intent(this,AdvisoryAlertActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.layoutRegulatoryUpdate.setOnClickListener {
            Intent(this,RegulatoryUpdateActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}