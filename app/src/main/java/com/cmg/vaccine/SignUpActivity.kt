package com.cmg.vaccine

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivitySignUpBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.util.showDatePickerDialog
import com.cmg.vaccine.viewmodel.SignupViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SignUpModelFactory
import com.hbb20.CountryCodePicker
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var viewModel:SignupViewModel
    private val signUpModelFactory:SignUpModelFactory by instance()
    val LOCATION:Int = 1
    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this,signUpModelFactory).get(SignupViewModel::class.java)
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

        binding.ccpLoadCountryCode.registerCarrierNumberEditText(binding.edtMobile)

        binding.ccpLoadCountryCode.setOnCountryChangeListener {
            viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode) }


        binding.edtDob.setOnClickListener {
            showDatePickerDialog(binding.edtDob)
        }

        binding.edtDobTime.setOnClickListener {
            showTimepickerDialog(binding.edtDobTime)
        }

        binding.edtEmail1.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())){
                    binding.edtEmail1.error = "Invalid Email"
                }
            }
        })

        binding.edtEmail2.addTextChangedListener(object :TextWatcher{
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                viewModel.setCurrentCountry(getCurrentCountryName()!!)
                //toast(getCurrentCountryName()!!)
            } else {
                requestPermission()
            }
        }
    }

    private fun getCurrentCountryName():String?{
        var country:String = ""
        val geocoder = Geocoder(applicationContext)
        for (provider in locationManager.allProviders){
            @SuppressWarnings("ResourceType")
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null){
                val address:List<Address> = geocoder.getFromLocation(location.latitude,location.longitude,1)
                if (!address.isNullOrEmpty()){
                    country = address.get(0).countryName
                }
            }
        }
        return country
    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            LOCATION ->{
                if (grantResults.isNotEmpty()){
                    val locationAccepted: Boolean =
                            grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (locationAccepted){
                        viewModel.setCurrentCountry(getCurrentCountryName()!!)
                        //toast(getCurrentCountryName()!!)
                    }else{
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