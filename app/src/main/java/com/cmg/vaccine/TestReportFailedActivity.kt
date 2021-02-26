package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityTestReportFailedBinding
import com.cmg.vaccine.util.Passparams

class TestReportFailedActivity : BaseActivity() {

    private lateinit var binding:ActivityTestReportFailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_report_failed)

        binding.imgBack.setOnClickListener {
            finish()
        }

        val testName = intent.extras?.getString(Passparams.TEST_REPORT_ID,"")

        binding.txtHeading.text = testName
    }
}