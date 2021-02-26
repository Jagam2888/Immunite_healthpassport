package com.cmg.vaccine

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityTestReportDetailBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.viewmodel.VaccineAndTestViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.VaccineAndTestModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TestReportDetailActivity : BaseActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:ActivityTestReportDetailBinding
    private lateinit var viewModel:VaccineAndTestViewModel

    private val factory:VaccineAndTestModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_report_detail)
        viewModel = ViewModelProvider(this,factory).get(VaccineAndTestViewModel::class.java)

        binding.viewmodel = viewModel

        binding.lifecycleOwner = this

        val testReport = intent.extras?.getString(Passparams.TEST_REPORT_ID,"")
        viewModel.loadData(testReport!!)




        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}