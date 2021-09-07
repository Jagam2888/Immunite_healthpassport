package com.cmg.vaccine

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityOTPVerifyBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.receiver.OTPReceiver
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.OTPVerifyViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.OTPVerifyModelFactory
import io.paperdb.Paper

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.regex.Matcher
import java.util.regex.Pattern

class OTPVerifyActivity : BaseActivity(),KodeinAware,SimpleListener{
    override val kodein by kodein()
    private lateinit var binding:ActivityOTPVerifyBinding
    private lateinit var viewModel:OTPVerifyViewModel
    //var isExistUser:Boolean?=null
    var navigateFrom:String?=null

    private val factory:OTPVerifyModelFactory by instance()

    companion object{
        const val SMS_PERMISSION = 1000
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_o_t_p_verify)
        viewModel = ViewModelProvider(this, factory).get(OTPVerifyViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this
        viewModel.listener = this

        navigateFrom = intent.extras?.getString(Passparams.NAVIGATE_FROM,"")
        viewModel.navigateFrom.set(navigateFrom)

        val subsId = intent.extras?.getString(Passparams.SUBSID,"")
        viewModel.userSubId.value = subsId
        viewModel.showMobileNumber()

        if (checkPermission()){
            //viewModel.txtOTP.set(OTPReceiver().getOTPValue())
            //OTPReceiver(viewModel)
            OTPReceiver().setEditText(viewModel._txtOTP,viewModel.pinTxt)
        }else{
            requestPermission()
        }

        startTimer()

        binding.txtResendOtp.setOnSingleClickListener {
            viewModel.callOTPTac()
            startTimer()
            binding.txtResendOtp.visibility = View.GONE
        }

        binding.edtTxt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if (!editable.isNullOrEmpty()) {
                    if (editable.length == 6) {
                        hideKeyBoard()
                        viewModel.onClick()
                    }
                }
            }
        })

        binding.btnActivate.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClick()
                return@OnEditorActionListener true
            }
            false
        })


    }


    private fun startTimer(){
        val timer = object :CountDownTimer(60000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                val value = millisUntilFinished / 1000
                val formatted = "${(value / 60).toString().padStart(2, '0')} : ${(value % 60).toString().padStart(
                    2,
                    '0'
                )}"
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

    private fun appendTxt(value: String){
        if (value.isNotEmpty()){
            binding.edtTxt.setText(binding.edtTxt.text.toString() + value)
        }else{
            binding.edtTxt.setText(value)
        }
    }

    private fun checkPermission():Boolean{
        return ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED))
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECEIVE_SMS),
            SMS_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            SMS_PERMISSION -> {
                if (grantResults.isNotEmpty()) {
                    val accepted: Boolean =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (accepted) {
                        OTPReceiver().setEditText(viewModel._txtOTP,viewModel.pinTxt)
                    } else {
                        toast("Permission Denied")
                    }
                }
            }
        }
    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        //toast(msg)
        if (navigateFrom.equals(Passparams.SIGNUP)) {
            /*Intent(this, SignupCompleteActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }*/
            Intent(this, VerifyFaceIDActivity::class.java).also {
                Paper.book().write(Passparams.NAVIGATE_FACE_ID,Passparams.SIGNUP)
                //it.putExtra(Passparams.NAVIGATE_FACE_ID,Passparams.SIGNUP)
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }else if(navigateFrom.equals(Passparams.FORGOT_PIN)){
            Intent(this, LoginPinActivity::class.java).also {
                it.putExtra(Passparams.ISCREATE,"create")
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }else{
            Intent(this, CheckOutActivity::class.java).also {
                //it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                finish()
            }
        }
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        //toast(msg)
        if (msg.startsWith("1")){
         val msgSplitArray = msg.split("|")
         if (msgSplitArray.size > 1){
             showAlertDialog(resources.getString(R.string.otp), msgSplitArray[2], msgSplitArray[1].toBoolean(), supportFragmentManager)
         }
        }else if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialog(resources.getString(R.string.failed), showMsg, false, supportFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(msg, "", false, supportFragmentManager)
        }
    }
}