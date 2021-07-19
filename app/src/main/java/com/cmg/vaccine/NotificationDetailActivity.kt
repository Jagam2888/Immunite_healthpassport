package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityNotificationDetailBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.changeDateFormatForNotification

class NotificationDetailActivity : BaseActivity() {
    private lateinit var binding:ActivityNotificationDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification_detail)

        val notificationFrom = intent.extras?.getString(Passparams.NOTIFICATION_FROM,"")
        val msg = intent.extras?.getString(Passparams.NOTIFICATION_MSG,"")
        var date = intent.extras?.getString(Passparams.NOTIFICATION_DATE,"")
        if (!date.isNullOrEmpty()){
            date = changeDateFormatForNotification(date)
        }

        when(notificationFrom){
            "N" ->{
                binding.txtActionbar.text = resources.getString(R.string.news_update)
                binding.txtTitleNews.text = msg
                binding.txtDateNews.text = date
                binding.layoutNewsUpdate.visibility = View.VISIBLE
                binding.layoutRegulatoryUpdate.visibility = View.GONE
                binding.layoutAdvisoryAlert.visibility = View.GONE
            }
            "R" -> {
                binding.txtActionbar.text = resources.getString(R.string.regulatory)
                binding.txtTitleRegulatory.text = msg
                binding.txtDateRegular.text = date
                binding.layoutRegulatoryUpdate.visibility = View.VISIBLE
                binding.layoutNewsUpdate.visibility = View.GONE
                binding.layoutAdvisoryAlert.visibility = View.GONE
            }
            "A" -> {
                binding.txtActionbar.text = resources.getString(R.string.advisory)
                binding.txtTitleAdvisory.text = msg
                binding.txtDateAdvisory.text = date
                binding.layoutAdvisoryAlert.visibility = View.VISIBLE
                binding.layoutNewsUpdate.visibility = View.GONE
                binding.layoutRegulatoryUpdate.visibility = View.GONE
            }
            else -> binding.txtActionbar.text = "Invalid"
        }




        //val title = intent.extras?.get("title")
        //binding.txtTitle.text = title.toString()

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}