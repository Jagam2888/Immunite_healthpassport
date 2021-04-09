package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityLoginPinBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.hideKeyBoard
import com.cmg.vaccine.util.showAlertDialog
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.LoginPinViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.LoginPinViewFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginPinActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityLoginPinBinding
    private lateinit var viewModel:LoginPinViewModel

    private val factory:LoginPinViewFactory by instance()
    private var loginStatus:String?=null
    var userSubId:String?=null
    //private var isComeFromSettings:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_pin)
        viewModel = ViewModelProvider(this,factory).get(LoginPinViewModel::class.java)
        binding.pinviewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.listener = this

        binding.edtTxt.requestFocus()



        loginStatus = intent.extras?.getString(Passparams.ISCREATE,"")
        viewModel.status.set(loginStatus)

        if (loginStatus.isNullOrEmpty()){
            binding.txtForgotpin.visibility = View.VISIBLE
            userSubId = viewModel.getSubId()

            if (binding.actionBar1.visibility == View.VISIBLE)
                binding.actionBar1.visibility = View.GONE

            if (binding.actionBar2.visibility == View.GONE)
                binding.actionBar2.visibility = View.VISIBLE
        }else{
            if (binding.actionBar2.visibility == View.VISIBLE)
                binding.actionBar2.visibility = View.GONE

            if (binding.actionBar1.visibility == View.GONE)
                binding.actionBar1.visibility = View.VISIBLE
        }

        //isComeFromSettings = intent.extras?.getBoolean(Passparams.ISSETTINGS) == true

        viewModel.loadValues(this)

        initViews()

        binding.edtTxt.requestFocus()

        binding.txtForgotpin.setOnSingleClickListener {
            Intent(this, OTPVerifyActivity::class.java).also {
                it.putExtra(Passparams.NAVIGATE_FROM, Passparams.FORGOT_PIN)
                it.putExtra(Passparams.SUBSID, userSubId)
                startActivity(it)

            }
        }
    }

    private fun initViews(){

        binding.edtTxt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if (viewModel.getPin.value != null && loginStatus ==""){
                    if (editable != null) {
                        if (editable.length == 4){
                            //if (!isDoneReEnter) {
                                if (viewModel.getPin.value!!.pin.equals(editable.toString())){
                                    hideKeyBoard()
                                Intent(this@LoginPinActivity,MainActivity::class.java).also {
                                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(it)
                                }
                                }else{
                                    toast("Incorrect PIN")
                                }
                            /*}else{
                                binding.edtTxt.setText("")
                                isDoneReEnter = true
                            }*/
                        }
                    }
                }
            }
        })

        /*binding.btnConfirm.setOnClickListener {
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
        }*/
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
    }

    override fun onSuccess(msg: String) {
        //toast(msg)
        hideKeyBoard()
        if (loginStatus == "create") {
            Intent(this@LoginPinActivity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                finish()
            }
        }else if (loginStatus == "update"){
            finish()
        }
    }

    override fun onShowToast(msg: String) {
        toast(msg)
    }

    override fun onFailure(msg: String) {
        showAlertDialog(resources.getString(R.string.login_failed), msg, false, supportFragmentManager)
    }
}