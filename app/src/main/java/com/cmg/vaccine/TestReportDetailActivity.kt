package com.cmg.vaccine


import android.os.Bundle
import android.os.StrictMode

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityTestReportDetailBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.VaccineAndTestViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.VaccineAndTestModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File


class TestReportDetailActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityTestReportDetailBinding
    private lateinit var viewModel:VaccineAndTestViewModel

    private val factory:VaccineAndTestModelFactory by instance()

    companion object{
        const val WRITE_STORAGE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode
                .setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())

        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_report_detail)
        viewModel = ViewModelProvider(this,factory).get(VaccineAndTestViewModel::class.java)

        binding.viewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.listener = this

        val testReport = intent.extras?.getString(Passparams.TEST_REPORT_ID,"")
        viewModel.loadData(testReport!!)




        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnFileDownload.setOnSingleClickListener {
            //viewModel.download(it)
            val fileName = viewModel.recorId.value!!
            val filePath = filesDir.absolutePath.toString()+"/TestReport"
            val file =  File(filePath, "$fileName.pdf")
            if (file.exists()) {
                openPdf(fileName, file)
            }else{
                viewModel.download(it)
            }

        }
    }



    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        val fileName = viewModel.recorId.value!!
        val filePath = filesDir.absolutePath.toString()+"/TestReport"
        val file =  File(filePath, "$fileName.pdf")
        if (file.exists()) {
            openPdf(fileName, file)
        }

    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        //toast(msg)
        showAlertDialog(msg,"",false,supportFragmentManager)
    }
}