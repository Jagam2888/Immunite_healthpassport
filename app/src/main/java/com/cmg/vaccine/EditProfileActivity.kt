package com.cmg.vaccine

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.databinding.ActivityEditProfileBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.ProfileViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ProfileViewModelFactory
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class EditProfileActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityEditProfileBinding
    private lateinit var viewModel: ProfileViewModel

    private val factory: ProfileViewModelFactory by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        binding.profileviewmodel = viewModel
        //binding.lifecycleOwner = this
        viewModel.listener = this
        viewModel.loadParentData()
        //viewModel.setCurrentCountry(getCurrentCountryName()!!)
        if (viewModel.countryCode.value != null)
            binding.ccpLoadCountryCode.setCountryForPhoneCode(viewModel.countryCode.value!!)

        binding.ccpLoadCountryCode.registerCarrierNumberEditText(binding.edtMobile)
        binding.imgBack.setOnClickListener {
            finish()
        }
        viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode)
        binding.ccpLoadCountryCode.setOnCountryChangeListener {
            viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode) }

        //binding.edtDob.listen()

        binding.edtDob.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawableClickListener.DrawablePosition?) {
                when (target) {
                    DrawableClickListener.DrawablePosition.RIGHT -> {
                        hideKeyBoard()
                        showDatePickerDialog(binding.edtDob)
                    }
                    else -> {
                    }
                }
            }
        })

        /*binding.edtDob.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length > 1){
                    if (!validateDateFormat(s.toString())){
                        binding.edtDob.error = "Sorry! Invalid Date of Birth"
                    }
                }
            }
        })*/

        viewModel.countries.observe(this, Observer { list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.edtPlaceBirth.adapter = CountryListAdapter(arrayList)
            binding.spinnerNationality.adapter = CountryListAdapter(arrayList)
        })

        binding.edtDobTime.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawableClickListener.DrawablePosition?) {
                when (target) {
                    DrawableClickListener.DrawablePosition.RIGHT -> {
                        hideKeyBoard()
                        showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
                    }
                    else -> {
                    }
                }
            }
        })

        binding.layoutImg.setOnClickListener {
            if (checkPermission()){
                //pickImageFromGallery()
                cropImage()
            }else{
                requestPermission()
            }
        }

        binding.edtMobile.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.edtDob.requestFocus()
                return@OnEditorActionListener true
            }
            false
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

        /*binding.edtDob.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length <=10) {
                    if (!validateDateFormat(s.toString())) {
                        binding.edtDob.error = "Invalid Date of Birth"
                    }
                }
            }
        })*/

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


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object{
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val accepted: Boolean =
                            grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (accepted) {
                        //pickImageFromGallery()
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
        /*if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Picasso.with(this)
                    .load(data?.data)
                    .centerCrop()
                    .fit()
                    .into(binding.imgGallery)
        }*/
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri = result.uri
                binding.headPicture.setImageURI(resultUri)
                toast("You profile picture was successfully changed")
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.message?.let { toast(it) }
            }
        }

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

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        Intent(this, OTPVerifyActivity::class.java).also {
            it.putExtra("IsExistUser", true)
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