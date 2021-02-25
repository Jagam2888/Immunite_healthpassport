package com.cmg.vaccine

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.Country
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.databinding.ActivitySignUpBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.util.DrawableClickListener.DrawablePosition
import com.cmg.vaccine.viewmodel.SignupViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SignUpModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*


class SignUpActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var viewModel:SignupViewModel
    private val signUpModelFactory:SignUpModelFactory by instance()
    var fusedLocationProviderClient:FusedLocationProviderClient?=null
    var current = ""
    val ddmmyyyy = "dd/mm/yy"
    val cal = Calendar.getInstance()

    companion object{
        const val LOCATION:Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this, signUpModelFactory).get(SignupViewModel::class.java)
        binding.signupviewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.listener = this
        viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode)
        //viewModel.loadYears()
        //viewModel.loadDays()

        /*binding.btnSignup.setOnClickListener {
            Intent(this,TellUsMoreActivity::class.java).also {
                startActivity(it)
            }
        }*/

        /*binding.txtLogin.setOnClickListener {
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
            }
        }*/

        //binding.edtDob.listen()

        viewModel.countries.observe(this, androidx.lifecycle.Observer {list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.spinnerPlaceBirth.adapter = CountryListAdapter(arrayList)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermission()) {
                    viewModel.setCurrentCountry(getCurrentCountryName()!!)
                    //toast(getCurrentCountryName()!!)
                    //getLastLocation()
                } else {
                    requestPermission()
                }
            }
        })

        binding.edtDob.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawablePosition?) {
                when (target) {
                    DrawablePosition.RIGHT -> {
                        showDatePickerDialog(binding.edtDob)
                    }
                    else -> {
                    }
                }
            }
        })

        binding.edtDobTime.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawablePosition?) {
                when (target) {
                    DrawablePosition.RIGHT -> {
                        showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
                    }
                    else -> {
                    }
                }
            }
        })

        binding.edtMobile.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtDob.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })


        binding.edtDob.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtDobTime.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })
        binding.edtDobTime.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtEmail1.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding.ccpLoadCountryCode.registerCarrierNumberEditText(binding.edtMobile)

        binding.ccpLoadCountryCode.setOnCountryChangeListener {
            viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode) }


        /*binding.edtDob.setOnClickListener {
            showDatePickerDialog(binding.edtDob)
        }*/

        /*binding.edtDobTime.setOnClickListener {
            showTimepickerDialog(binding.edtDobTime)
        }*/



        binding.edtEmail1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())) {
                    binding.edtEmail1.error = "Invalid Email"
                }
            }
        })

        binding.edtEmail2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())) {
                    binding.edtEmail2.error = "Invalid Email"
                }
            }
        })

       /* binding.edtPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidPassword(s.toString()))
                    binding.edtPassword.error = "Password must Minimum 4 character"
            }
        })*/


    }

    private fun checkPermission():Boolean{
        return ((ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED))
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            LOCATION -> {
                if (grantResults.isNotEmpty()) {
                    val locationAccepted: Boolean =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (locationAccepted) {
                        viewModel.setCurrentCountry(getCurrentCountryName()!!)
                        //toast(getCurrentCountryName()!!)
                    } else {
                        toast("Permission Denied, You cannot get Location")
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
        if (!msg.isNullOrEmpty()) {
            Intent(this, TellUsMoreActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }
}