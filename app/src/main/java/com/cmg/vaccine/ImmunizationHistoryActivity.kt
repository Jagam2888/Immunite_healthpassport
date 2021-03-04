package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityImmunizationHistoryBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.ImmunizationHistoryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ImmunizationHistoryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ImmunizationHistoryActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()


    private lateinit var binding:ActivityImmunizationHistoryBinding
    private lateinit var viewModel:ImmunizationHistoryViewModel

    private val immunizationHistoryViewModelFactory:ImmunizationHistoryViewModelFactory by instance()
    var lastClickTimeDOB:Long = 0
    var lastClickTimeDOBTime:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_immunization_history)

        viewModel = ViewModelProvider(this,immunizationHistoryViewModelFactory).get(ImmunizationHistoryViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.listener = this
        viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode)

        binding.edtDob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateDateFormat(binding.edtDob.text.toString())) and (binding.edtDob.text?.isNotEmpty() == true)){
                    binding.edtDob.error = "Sorry! Invalid Date"
                }else{
                    binding.edtDob.error = null
                }
            }
        })

        binding.edtDobTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateTime(binding.edtDobTime.text.toString())) and (binding.edtDobTime.text?.isNotEmpty() == true)){
                    binding.edtDobTime.error = "Sorry! Invalid Time"
                }else{
                    binding.edtDobTime.error = null
                }
            }
        })

        binding.btnDobCalender.setOnClickListener {
            hideKeyBoard()
            if (SystemClock.elapsedRealtime() - lastClickTimeDOB<1000){
                return@setOnClickListener
            }
            Log.d("onclick","come here")
            lastClickTimeDOB = SystemClock.elapsedRealtime()
            showDatePickerDialog(binding.edtDob)
        }

        binding.btnDobTimeCalender.setOnClickListener {
            hideKeyBoard()
            if (SystemClock.elapsedRealtime() - lastClickTimeDOBTime<1000){
                return@setOnClickListener
            }
            Log.d("onclickdob","come here")
            lastClickTimeDOBTime = SystemClock.elapsedRealtime()
            showTimepickerDialog(binding.edtDobTime, "1200")
        }


        binding.ccpLoadCountryCode.registerCarrierNumberEditText(binding.edtMobile)

        binding.ccpLoadCountryCode.setOnCountryChangeListener {
            viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode) }

        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        Intent(this,SuccessPopUpActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }
}