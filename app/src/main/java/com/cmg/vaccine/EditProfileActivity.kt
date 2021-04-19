package com.cmg.vaccine

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
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
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.cmg.vaccine.DialogFragment.CountryListDialogFragment
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityEditProfileBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.ProfileViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ProfileViewModelFactory
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity(),KodeinAware,SimpleListener,SlideDatePickerDialogCallback {
    override val kodein by kodein()
    private lateinit var binding:ActivityEditProfileBinding
    private lateinit var viewModel: ProfileViewModel

    private val factory: ProfileViewModelFactory by instance()

    var lastClickTimeDOB:Long = 0
    var lastClickTimeDOBTime:Long = 0
    var isDOBPicker:Boolean = false

    companion object{
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        binding.profileviewmodel = viewModel
        binding.lifecycleOwner = this
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

        /*binding.edtDob.setDrawableClickListener(object : DrawableClickListener {
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
        })*/

        binding.checkboxTerms.movementMethod = LinkMovementMethod.getInstance()

        binding.checkboxTerms.setOnSingleClickListener{
            Intent(this,TermsOfUseActivity::class.java).also {
                startActivity(it)
            }
        }

        /*binding.btnDobCalender.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTimeDOB<1000){
                return@setOnClickListener
            }
            Log.d("onclickdob","come here")
            lastClickTimeDOB = SystemClock.elapsedRealtime()
            hideKeyBoard()
            showDatePickerDialog(binding.edtDob)
        }*/

        /*binding.btnDobCalender.setOnSingleClickListener{
            hideKeyBoard()
            isDOBPicker = true
            showSliderDatePickerDialog("DOB",supportFragmentManager,
                Calendar.getInstance().apply {
                    set(Calendar.YEAR,1900)
                }, Calendar.getInstance())
            //showDatePickerDialog(binding.edtDob)
        }

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
        })*/

        binding.btnDateCalender.setOnSingleClickListener{
            hideKeyBoard()
            isDOBPicker = false
            showSliderDatePickerDialog("passport",supportFragmentManager,
                Calendar.getInstance().apply { add(Calendar.DATE,1) }, Calendar.getInstance().apply { add(Calendar.YEAR,10) })
            //showDatePickerDialogForPassport(binding.edtPassportExpDate)
        }

        /*binding.btnDateCalender.setOnClickListener {
            showDatePickerDialogForPassport(binding.edtPassportExpDate)
        }*/

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

        binding.ccpPob.setCountryForNameCode(viewModel.birthPlaceCountryCode.value?.dropLast(1))
        binding.ccpPob.setOnCountryChangeListener {
            viewModel.birthPlaceCountryCode.value = getThreeAlpha(binding.ccpPob.selectedCountryNameCode)
        }

        binding.ccpNationality.setCountryForNameCode(viewModel.nationalityCountryCode.value?.dropLast(1))
        binding.ccpNationality.setOnCountryChangeListener {
            viewModel.idNo.value = ""
            if (getThreeAlpha(binding.ccpNationality.selectedCountryNameCode) == "MYS"){
                viewModel.patientIdNoCharLength.set(12)
            }else{
                viewModel.patientIdNoCharLength.set(15)
            }
            viewModel.nationalityCountryCode.value = getThreeAlpha(binding.ccpNationality.selectedCountryNameCode)
        }

        /*binding.layoutNationality.setOnSingleClickListener{
            var myDialogFragment= CountryListDialogFragment()
            var data=Bundle()
            data.putString("type","nation")
            data.putString("from","edit_profile")
            myDialogFragment.arguments=data
            myDialogFragment.show(supportFragmentManager,"Place of Birth")

        }

        binding.layoutPob.setOnSingleClickListener{
            var myDialogFragment= CountryListDialogFragment()
            var data=Bundle()
            data.putString("type","pob")
            data.putString("from","edit_profile")
            myDialogFragment.arguments=data
            myDialogFragment.show(supportFragmentManager,"Place of Birth")

        }*/

        /*viewModel.countries.observe(this, Observer { list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.edtPlaceBirth.adapter = CountryListAdapter(arrayList)
            binding.spinnerNationality.adapter = CountryListAdapter(arrayList)
        })*/

        /*binding.edtDobTime.setDrawableClickListener(object : DrawableClickListener {
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
        })*/

        /*binding.btnDobTimeCalender.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTimeDOBTime<1000){
                return@setOnClickListener
            }
            Log.d("onclick","come here")
            lastClickTimeDOBTime = SystemClock.elapsedRealtime()
            hideKeyBoard()
            showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
        }*/

        binding.btnDobTimeCalender.setOnSingleClickListener{
            hideKeyBoard()
            var hour = 12
            var minute = 0
            if (!viewModel.dobTime.value.isNullOrEmpty()){
                val dobTimeArray = viewModel.dobTime.value!!.split(":")
                if (dobTimeArray.size > 1){
                    hour = dobTimeArray[0].toInt()
                    minute = dobTimeArray[1].toInt()
                }
            }
            showSnapTimePickerDialog(hour,minute).apply {
                setListener { hour, minute -> onTimePicked(hour, minute,binding.edtDobTime) }
            }.show(supportFragmentManager, SnapTimePickerDialog.TAG)
            //showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
        }

        binding.layoutImg.setOnSingleClickListener{
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



        if (!viewModel.getProfileImage().isNullOrEmpty()){
            val uri = Uri.parse(viewModel.getProfileImage())
            binding.headPicture.setImageURI(uri)
        }

        binding.edtIdno.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isNullOrEmpty()){
                    if (viewModel.nationalityCountryCode.value.equals("MYS",false)) {
                        if (s?.length!! < 12) {
                            binding.edtIdno.error = "Minimum 12 Character"
                        }
                    }else{
                        if (s?.length!! < 15) {
                            binding.edtIdno.error = "Minimum 15 Character"
                        }
                    }
                }
            }
        })
    }

    private fun cropImage() {
        CropImage.activity()
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
                val resultUri = result?.uri
                viewModel.saveProfileImage(resultUri.toString())
                //viewModel.profileImageUri.set(resultUri.toString())
                binding.headPicture.setImageURI(resultUri)
                //toast("You profile picture was successfully changed")
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result?.error
                error?.message?.let { toast(it) }
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

    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        if (isDOBPicker) {
            binding.edtDob.setText(SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
            binding.edtDob.setSelection(binding.edtDob.length())
        }else{
            binding.edtPassportExpDate.setText( SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
            binding.edtPassportExpDate.setSelection(binding.edtPassportExpDate.length())
        }
    }

    fun setNation(countryCode:String)
    {
        hideKeyBoard()
        viewModel.nationalityCountryCode.value = World.getCountryFrom(countryCode).name
        //selected_nation.country_name.text = country
        //binding.txtCountryNameNationality.text = World.getCountryFrom(countryCode).name
        viewModel.nationalityCountryFlag.value = World.getFlagOf(countryCode)
        //binding.imgCountryFlagNationlity.setImageResource(World.getFlagOf(countryCode))
        viewModel.idNo.value = ""
        if (countryCode == "MYS"){
            viewModel.patientIdNoCharLength.set(12)
        }else{
            viewModel.patientIdNoCharLength.set(15)
        }

    }

    fun setPOB(countryCode:String)
    {
        hideKeyBoard()
        viewModel.birthPlaceCountryCode.value = World.getCountryFrom(countryCode).name
        //binding.txtCountryNamePob.text = World.getCountryFrom(countryCode).name
        viewModel.birthPlaceCountryFlag.value = World.getFlagOf(countryCode)
        //binding.imgCountryFlagPob.setImageResource(World.getFlagOf(countryCode))

    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        //toast(msg)
        Intent(this, OTPVerifyActivity::class.java).also {
            it.putExtra(Passparams.NAVIGATE_FROM, Passparams.EDIT_PROFILE)
            it.putExtra(Passparams.SUBSID, viewModel.userSubId.value)
            startActivity(it)
            finish()
        }
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        if (msg.startsWith("1")){
            val msgSplitArray = msg.split("|")
            if (msgSplitArray.size > 1){
                showAlertDialog(resources.getString(R.string.otp), msgSplitArray[2], msgSplitArray[1].toBoolean(), supportFragmentManager)
            }
        }else if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialog(resources.getString(R.string.failed), showMsg, false, supportFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(msg, "", false, supportFragmentManager)
        }
    }
}