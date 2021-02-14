package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityOTPVerifyBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.OTPVerifyViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.OTPVerifyModelFactory
import kotlinx.android.synthetic.main.activity_o_t_p_verify.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class OTPVerifyActivity : AppCompatActivity(),KodeinAware,SimpleListener{
    override val kodein by kodein()
    private lateinit var binding:ActivityOTPVerifyBinding
    private lateinit var viewModel:OTPVerifyViewModel
    var isExistUser:Boolean?=null

    private val factory:OTPVerifyModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_o_t_p_verify)
        viewModel = ViewModelProvider(this,factory).get(OTPVerifyViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this
        viewModel.listener = this

        isExistUser = intent.extras?.getBoolean("IsExistUser")
        viewModel.isExistUser.value = isExistUser


        /*binding.btnOne.setOnClickListener {
            appendTxt(binding.btnOne.text.toString())
        }

        binding.btnTwo.setOnClickListener {
            appendTxt(binding.btnTwo.text.toString())
        }

        binding.btnThree.setOnClickListener {
            appendTxt(binding.btnThree.text.toString())
        }
        binding.btnFour.setOnClickListener {
            appendTxt(binding.btnFour.text.toString())
        }
        binding.btnFive.setOnClickListener {
            appendTxt(binding.btnFive.text.toString())
        }
        binding.btnSix.setOnClickListener {
            appendTxt(binding.btnSix.text.toString())
        }
        binding.btnSeven.setOnClickListener {
            appendTxt(binding.btnSeven.text.toString())
        }
        binding.btnEight.setOnClickListener {
            appendTxt(binding.btnEight.text.toString())
        }
        binding.btnNine.setOnClickListener {
            appendTxt(binding.btnNine.text.toString())
        }
        binding.btnZero.setOnClickListener {
            appendTxt(binding.btnZero.text.toString())
        }

        binding.btnX.setOnClickListener {
            removePin()
        }*/

        /*binding.btnActivate.setOnClickListener {
            Log.d("text",binding.edtTxt.text.toString())
            if (binding.edtTxt.text.toString()!!.isNotEmpty()) {
                if (binding.edtTxt.text.toString()!! == "123456") {

                }else{
                    toast("OTP is Wrong")
                }
            }
        }*/

        startTimer()

        binding.txtResendOtp.setOnClickListener {
            viewModel.onResendTac()
            startTimer()
            binding.txtResendOtp.visibility = View.GONE
        }


    }

    private fun startTimer(){
        val timer = object :CountDownTimer(300000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val value = millisUntilFinished / 1000
                val formatted = "${(value / 60).toString().padStart(2, '0')} : ${(value % 60).toString().padStart(2, '0')}"
                binding.txtTimer.text = formatted
            }

            override fun onFinish() {
                binding.txtResendOtp.visibility = View.VISIBLE
            }
        }
        timer.start()
    }

    private fun removePin(){
        var value = binding.edtTxt.text.toString()
        if (value!!.isNotEmpty()){
            binding.edtTxt.setText(value.dropLast(1))
        }
    }

    private fun appendTxt(value:String){
        if (value.isNotEmpty()){
            binding.edtTxt.setText(binding.edtTxt.text.toString() + value)
        }else{
            binding.edtTxt.setText(value)
        }
    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        if (!isExistUser!!) {
            Intent(this, SubscriptionActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(it)
            }
        }else{
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(it)
            }
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }
}