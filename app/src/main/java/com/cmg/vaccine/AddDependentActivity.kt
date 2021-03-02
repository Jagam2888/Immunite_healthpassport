package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.Country
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.databinding.ActivityAddDependentBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.DependentViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.DependentViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AddDependentActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityAddDependentBinding
    private lateinit var viewModel:DependentViewModel

    private val factory:DependentViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_dependent)

        viewModel = ViewModelProvider(this,factory).get(DependentViewModel::class.java)
        binding.dependentViewModel = viewModel

        binding.lifecycleOwner = this
        viewModel.listener = this

        viewModel.countries.observe(this, Observer {list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.spinnerPlaceBirth.adapter = CountryListAdapter(arrayList)
            binding.spinnerNationality.adapter = CountryListAdapter(arrayList)
        })




        //binding.edtDob.listen()

        binding.edtDob.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawableClickListener.DrawablePosition?) {
                when (target) {
                    DrawableClickListener.DrawablePosition.RIGHT -> {
                        showDatePickerDialog(binding.edtDob)
                    }
                    else -> {
                    }
                }
            }
        })

        binding.edtDobTime.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawableClickListener.DrawablePosition?) {
                when (target) {
                    DrawableClickListener.DrawablePosition.RIGHT -> {
                        showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
                    }
                    else -> {
                    }
                }
            }
        })

        /*binding.addressCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                viewModel.address.set(viewModel.parentAddress)
                viewModel.city.set(viewModel.parentcity)
                viewModel.state.set(viewModel.parentState)
            }else{
                viewModel.address.set("")
            }
        }*/
        if (viewModel.countryCode.value != null)
            binding.ccpLoadCountryCode.setCountryForPhoneCode(viewModel.countryCode.value!!)
        viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode)
        binding.ccpLoadCountryCode.registerCarrierNumberEditText(binding.edtMobile)

        binding.ccpLoadCountryCode.setOnCountryChangeListener {
            viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode) }



        binding.edtMobile.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtDob.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())){
                    binding.edtEmail.error = "Invalid Email"
                }
            }
        })
        binding.edtEmail2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())){
                    binding.edtEmail2.error = "Invalid Email"
                }
            }
        })

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.radio_new_member ->{
                    if (binding.layoutNewMembers.visibility == View.GONE) {
                        binding.layoutNewMembers.visibility = View.VISIBLE
                    }
                        binding.layoutExistingUser.visibility = View.GONE
                }
                else ->{
                    if (binding.layoutExistingUser.visibility == View.GONE) {
                        binding.layoutExistingUser.visibility = View.VISIBLE
                    }
                        binding.layoutNewMembers.visibility = View.GONE

                }
            }
        }

        binding.edtDobTime.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtEmail.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding.edtDob.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtDobTime.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        finish()
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setCurrentCountry(getCurrentCountryName()!!)
    }
}