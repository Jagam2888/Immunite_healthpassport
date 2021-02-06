package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityLoginPinBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.LoginPinViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.LoginPinViewFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginPinActivity : AppCompatActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityLoginPinBinding
    private lateinit var viewModel:LoginPinViewModel

    private val factory:LoginPinViewFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_pin)
        viewModel = ViewModelProvider(this,factory).get(LoginPinViewModel::class.java)
        binding.pinviewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.listener = this

        val isCreate = intent.extras?.getBoolean("isCreate",false)
        viewModel.isCreate.value = isCreate

        initViews()
    }

    private fun initViews(){
        binding.btnOne.setOnClickListener {
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
        }

        binding.edtTxt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if (viewModel.getPin.value != null){
                    if (editable != null) {
                        if (editable.length == 4){
                            //if (!isDoneReEnter) {
                                if (viewModel.getPin.value!!.pin.equals(editable.toString())){
                                Intent(this@LoginPinActivity,MainActivity::class.java).also {
                                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(it)
                                    finish()
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
        toast(msg)
        Intent(this@LoginPinActivity,MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}