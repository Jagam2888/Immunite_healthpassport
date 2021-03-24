package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityDepartureVerificationBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.DepartureVerificationViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.DepartureVerificationModelFactory
import io.paperdb.Paper
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class DepartureVerificationActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityDepartureVerificationBinding
    private lateinit var viewModel:DepartureVerificationViewModel

    private val factory:DepartureVerificationModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_departure_verification)

        viewModel = ViewModelProvider(this,factory).get(DepartureVerificationViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        val qrCodeValue = Paper.book().read<String>(Passparams.QR_CODE_VALUE,"")
        viewModel.qrCodeValue.value = qrCodeValue.replace("\\n","\n")

        viewModel.loadData()

        binding.imgBack.setOnClickListener {
            finish()
        }

        startTimer()

    }

    private fun startTimer(){
        val timer = object : CountDownTimer(15000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val value = millisUntilFinished / 1000
                val formatted = "${(value / 60).toString().padStart(2, '0')} : ${(value % 60).toString().padStart(2, '0')}"
                if (!formatted.isNullOrEmpty()){
                    binding.txtTimer.text = formatted
                }
            }

            override fun onFinish() {
                finish()
            }
        }
        timer.start()
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}