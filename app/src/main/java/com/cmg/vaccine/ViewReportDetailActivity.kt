package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityViewReportDetailBinding

class ViewReportDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityViewReportDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_report_detail)

        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}