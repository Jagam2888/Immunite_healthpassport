package com.cmg.vaccine

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.Country
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.databinding.ActivityTellUsMoreBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.TellUsMoreViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.TellUsViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TellUsMoreActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityTellUsMoreBinding
    private lateinit var viewModel:TellUsMoreViewModel
    private val factory:TellUsViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_tell_us_more)
        viewModel = ViewModelProvider(this,factory).get(TellUsMoreViewModel::class.java)
        binding.tellusviewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        viewModel.countries.observe(this, androidx.lifecycle.Observer {list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.spinnerNationality.adapter = CountryListAdapter(arrayList)
            viewModel.getPlaceOfBirthPos()
        })

        //viewModel.setPlaceOfBirthToNationality()

        /*binding.btnSignup.setOnClickListener {
            Intent(this,OTPVerifyActivity::class.java).also {
                startActivity(it)
            }
        }*/

        binding.btnDateCalender.setOnClickListener {
            showDatePickerDialogForPassport(binding.edtPassportExpDate)
        }

        binding.edtPassportExpDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateDateFormatForPassport(binding.edtPassportExpDate.text.toString())) and (binding.edtPassportExpDate.text?.isNotEmpty() == true)){
                    binding.edtPassportExpDate.error = "Sorry! Invalid Date"
                }else{
                    binding.edtPassportExpDate.error = null
                }
            }
        })
    }

    override fun onStarted() {
        show(binding.progressTellus)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressTellus)
        toast(msg)
        Intent(this,OTPVerifyActivity::class.java).also {
            it.putExtra(Passparams.NAVIGATE_FROM,Passparams.SIGNUP)
            it.putExtra(Passparams.SUBSID, viewModel.userSubId.value)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressTellus)
        toast(msg)
    }
}