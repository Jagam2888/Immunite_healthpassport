package com.cmg.vaccine

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.Country
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityEditDependentProfileBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.DependentViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.DependentViewModelFactory
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class EditDependentProfileActivity : BaseActivity(),KodeinAware,SimpleListener {

    override val kodein by kodein()
    private lateinit var binding:ActivityEditDependentProfileBinding
    private lateinit var viewModel: DependentViewModel
    var lastClickTimeDOB:Long = 0
    var lastClickTimeDOBTime:Long = 0
    private val factory: DependentViewModelFactory by instance()
    var dependentSubId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_dependent_profile)
        viewModel = ViewModelProvider(this,factory).get(DependentViewModel::class.java)
        binding.dependentViewModel = viewModel

        binding.lifecycleOwner = this
        viewModel.listener = this


        binding.checkboxTerms.movementMethod = LinkMovementMethod.getInstance()

        binding.checkboxTerms.setOnSingleClickListener{
            Intent(this,TermsOfUseActivity::class.java).also {
                startActivity(it)
            }
        }

        dependentSubId = intent.extras?.getString(Passparams.DEPENDENT_SUBID,"")


        viewModel.countries.observe(this, Observer {list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.spinnerPlaceBirth.adapter = CountryListAdapter(arrayList)
            binding.spinnerNationality.adapter = CountryListAdapter(arrayList)
            viewModel.loadProfileData(this,dependentSubId!!)
        })

        /*binding.btnDobCalender.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTimeDOB<1000){
                return@setOnClickListener
            }
            Log.d("onclickdob","come here")
            lastClickTimeDOB = SystemClock.elapsedRealtime()
            hideKeyBoard()
            showDatePickerDialog(binding.edtDob)
        }

        binding.btnDobTimeCalender.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTimeDOBTime<1000){
                return@setOnClickListener
            }
            Log.d("onclick","come here")
            lastClickTimeDOBTime = SystemClock.elapsedRealtime()
            hideKeyBoard()
            showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
        }
        binding.btnDateCalender.setOnClickListener {
            showDatePickerDialogForPassport(binding.edtPassportExpDate)
        }*/
        binding.btnDobCalender.setOnSingleClickListener{
            hideKeyBoard()
            showDatePickerDialog(binding.edtDob)
        }

        binding.btnDobTimeCalender.setOnSingleClickListener{
            hideKeyBoard()
            showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
        }

        binding.btnDateCalender.setOnSingleClickListener{
            hideKeyBoard()
            showDatePickerDialogForPassport(binding.edtPassportExpDate)
        }

        binding.edtPassportExpDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateDateFormatForPassport(binding.edtPassportExpDate.text.toString())) and (binding.edtPassportExpDate.text?.isNotEmpty() == true)){
                    binding.edtPassportExpDate.error = Passparams.DATE_FORMAT
                }else{
                    binding.edtPassportExpDate.error = null
                }
            }
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

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.edtMobile.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtDob.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding.ccpLoadCountryCode.registerCarrierNumberEditText(binding.edtMobile)
        if (viewModel.countryCode.value != null)
            binding.ccpLoadCountryCode.setCountryForPhoneCode(viewModel.countryCode.value!!)

        viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode)
        binding.ccpLoadCountryCode.setOnCountryChangeListener {
            viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode) }


        binding.edtEmail1.addTextChangedListener(object : TextWatcher {
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

        binding.edtRetype.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())){
                    binding.edtRetype.error = "Invalid Email"
                }
            }
        })

        binding.edtDobTime.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtEmail1.requestFocus()
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

        binding.layoutImg.setOnSingleClickListener {
            if (checkPermission()){
                cropImage()
            }else{
                requestPermission()
            }
        }

        if (!viewModel.getProfileImage(dependentSubId!!).isNullOrEmpty()){
            val uri = Uri.parse(viewModel.getProfileImage(dependentSubId!!))
            binding.headPicture.setImageURI(uri)
        }
    }
    private fun cropImage() {
        CropImage.activity( )
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Edit Photo")
            .setCropShape(CropImageView.CropShape.OVAL)
            .setFixAspectRatio(true)
            .setCropMenuCropButtonTitle("Done")
            .start(this)
    }
    companion object{
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_CODE
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE-> {
                if (grantResults.isNotEmpty()) {
                    val accepted: Boolean =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (accepted) {
                        cropImage()
                    } else {
                        toast("Permission Denied, You cannot access your gallery")
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri = result.uri
                //viewModel.profileImageUri.set(resultUri.toString())
                viewModel.saveProfileImage(resultUri.toString(),dependentSubId!!)
                binding.headPicture.setImageURI(resultUri)
                //toast("You profile picture was successfully changed")
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.message?.let { toast(it) }
            }
        }

    }
    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        //toast(msg)
        //finish()
        Intent(this, OTPVerifyActivity::class.java).also {
            it.putExtra(Passparams.NAVIGATE_FROM, Passparams.EDIT_DEPENDENT_PROFILE)
            it.putExtra(Passparams.SUBSID, viewModel.userSubId.value)
            startActivity(it)
            finish()
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }
}