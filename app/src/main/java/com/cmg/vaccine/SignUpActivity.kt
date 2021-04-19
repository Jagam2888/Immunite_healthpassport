package com.cmg.vaccine

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.lifecycle.ViewModelProvider
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.DialogFragment.CountryListDialogFragment
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivitySignUpBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.util.DrawableClickListener.DrawablePosition
import com.cmg.vaccine.viewmodel.SignupViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SignUpModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : BaseActivity(),KodeinAware,SimpleListener,SlideDatePickerDialogCallback {
    override val kodein by kodein()
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var viewModel:SignupViewModel
    private val signUpModelFactory:SignUpModelFactory by instance()
    var fusedLocationProviderClient:FusedLocationProviderClient?=null
    var current = ""
    val ddmmyyyy = "dd/mm/yy"
    val cal = Calendar.getInstance()
    var lastClickTimeDOB:Long = 0
    var lastClickTimeDOBTime:Long = 0

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

        binding.btnDobTimeCalender.setOnSingleClickListener {
            hideKeyBoard()
            //showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
            showSnapTimePickerDialog(12,0).apply {
                setListener { hour, minute -> onTimePicked(hour, minute,binding.edtDobTime) }
            }.show(supportFragmentManager, SnapTimePickerDialog.TAG)
        }

        binding.btnDobCalender.setOnSingleClickListener {
            hideKeyBoard()
            //showDatePickerDialog(binding.edtDob)
            showSliderDatePickerDialog("DOB",supportFragmentManager,
            Calendar.getInstance().apply {
                                         set(Calendar.YEAR,1900)
            }, Calendar.getInstance())
        }

        viewModel.birthPlaceCountryCode.value = getThreeAlpha(binding.ccpPob.selectedCountryNameCode)
        binding.ccpPob.setOnCountryChangeListener {
            viewModel.birthPlaceCountryCode.value = getThreeAlpha(binding.ccpPob.selectedCountryNameCode)
        }

        /*binding.layoutPob.setOnSingleClickListener{
            var myDialogFragment= CountryListDialogFragment()
            var data=Bundle()
            data.putString("type","pob")
            data.putString("from","sign_up")
            myDialogFragment.arguments=data
            myDialogFragment.show(supportFragmentManager,"Place of Birth")

        }*/

        /*if (checkPermission()) {
            setCurrentCountry()
        } else {
            requestPermission()
        }*/

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

        /*viewModel.countries.observe(this, androidx.lifecycle.Observer {list->
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
        })*/

        /*binding.edtDob.setDrawableClickListener(object : DrawableClickListener {
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
        })*/

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

        binding.edtDob.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateDateFormat(binding.edtDob.text.toString())) and (binding.edtDob.text?.isNotEmpty() == true)){
                    //binding.edtDob.error = "Sorry! Invalid Date of Birth"
                    binding.edtDob.error = Passparams.DATE_FORMAT
                }else{
                    binding.edtDob.error = null
                }
            }
        })

        binding.edtDobTime.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateTime(binding.edtDobTime.text.toString())) and (binding.edtDobTime.text?.isNotEmpty() == true)){
                    binding.edtDobTime.error = "Sorry! Invalid Birth Time"
                }else{
                    binding.edtDobTime.error = null
                }
            }
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

    private fun setCurrentCountry(){
        viewModel.birthPlaceCountryCode.value = getCurrentCountryName()

        viewModel.birthPlaceCountryFlag.value = World.getCountryFrom(getCurrentCountryName()).flagResource

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
                        setCurrentCountry()
                        //viewModel.setCurrentCountry(getCurrentCountryName()!!)
                        //toast(getCurrentCountryName()!!)
                    } else {
                        toast("Permission Denied, You cannot get Location")
                    }
                }
            }
        }
    }

    fun setPOB(countryCode:String)
    {
        viewModel.birthPlaceCountryCode.value = World.getCountryFrom(countryCode).name
        viewModel.birthPlaceCountryFlag.value = World.getFlagOf(countryCode)

    }

    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        binding.edtDob.setText( SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
        binding.edtDob.setSelection(binding.edtDob.length())
    }

    /*private fun onTimePicked(selectedHour: Int, selectedMinute: Int) {
        val hour = selectedHour.toString().padStart(2, '0')
        val minute = selectedMinute.toString().padStart(2, '0')
        binding.edtDobTime.setText(String.format(getString(R.string.selected_time_format, hour, minute)))
    }*/


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

    override fun onShowToast(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(msg, "", false, supportFragmentManager)
        }
    }
}