package com.cmg.vaccine

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.DialogFragment.CountryListDialogFragment
import com.cmg.vaccine.DialogFragment.DependentDialogFragment
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityAddDependentBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.DependentViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.DependentViewModelFactory
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback
import io.paperdb.Paper
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class AddDependentActivity : BaseActivity(),KodeinAware,SimpleListener,SlideDatePickerDialogCallback {
    override val kodein by kodein()
    private lateinit var binding:ActivityAddDependentBinding
    private lateinit var viewModel:DependentViewModel

    var lastClickTimeDOB:Long = 0
    var lastClickTimeDOBTime:Long = 0
    var lastClickTimeQr:Long = 0

    var qrCodeValue = ""
    var isDOBPicker:Boolean = false

    private val factory:DependentViewModelFactory by instance()


    companion object{
        const val LOCATION:Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_dependent)

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

        binding.layoutNationality.setOnSingleClickListener{
            var myDialogFragment= CountryListDialogFragment()
            var data=Bundle()
            data.putString("type","nation")
            data.putString("from","add_dep")
            myDialogFragment.arguments=data
            myDialogFragment.show(supportFragmentManager,"Place of Birth")

        }

        binding.layoutPob.setOnSingleClickListener{
            var myDialogFragment= CountryListDialogFragment()
            var data=Bundle()
            data.putString("type","pob")
            data.putString("from","add_dep")
            myDialogFragment.arguments=data
            myDialogFragment.show(supportFragmentManager,"Place of Birth")

        }

        if (checkPermission()) {
            //viewModel.setCurrentCountry(getCurrentCountryName()!!)
            setCurrentCountry()
        } else {
            requestPermission()
        }

        /*viewModel.countries.observe(this, Observer {list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.spinnerPlaceBirth.adapter = CountryListAdapter(arrayList)
            binding.spinnerNationality.adapter = CountryListAdapter(arrayList)
            if (checkPermission()) {
                viewModel.setCurrentCountry(getCurrentCountryName()!!)
            } else {
                requestPermission()
            }
        })*/

        binding.btnDobCalender.setOnSingleClickListener{
            isDOBPicker = true
            hideKeyBoard()
            showSliderDatePickerDialog("DOB",supportFragmentManager,
                Calendar.getInstance().apply {
                    set(Calendar.YEAR,1900)
                }, Calendar.getInstance())
            //showDatePickerDialog(binding.edtDob)
        }

        binding.btnDobTimeCalender.setOnSingleClickListener{
            hideKeyBoard()
            showSnapTimePickerDialog(12,0).apply {
                setListener { hour, minute -> onTimePicked(hour, minute,binding.edtDobTime) }
            }.show(supportFragmentManager, SnapTimePickerDialog.TAG)
            //showTimepickerDialog(binding.edtDobTime, viewModel.dobTime.value!!)
        }

        binding.btnDateCalender.setOnSingleClickListener{
            isDOBPicker = false
            hideKeyBoard()
            showSliderDatePickerDialog("passport",supportFragmentManager,
                Calendar.getInstance().apply { add(Calendar.DATE,1) }, Calendar.getInstance().apply { add(Calendar.YEAR,10) })
            //showDatePickerDialogForPassport(binding.edtPassportExpDate)
        }

        binding.edtPassportExpDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateDateFormatForPassport(binding.edtPassportExpDate.text.toString())) and (binding.edtPassportExpDate.text?.isNotEmpty() == true)){
                    //binding.edtPassportExpDate.error = "Sorry! Invalid Date"
                    binding.edtPassportExpDate.error = Passparams.DATE_FORMAT
                }else{
                    binding.edtPassportExpDate.error = null
                }
            }
        })

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

        binding.edtDob.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if ((!validateDateFormat(binding.edtDob.text.toString())) and (binding.edtDob.text?.isNotEmpty() == true)){
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

        binding.btnScanQr.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTimeQr<1000){
                return@setOnClickListener
            }
            lastClickTimeQr = SystemClock.elapsedRealtime()
            hideKeyBoard()
            if (!binding.edtDob.text.toString().isNullOrEmpty()) {
                Paper.book().write(Passparams.QR_CODE_VALUE, "")
                Intent(this, ScanQRActivity::class.java).also {
                    startActivity(it)
                }
            }else{
                toast("Please enter your Date of Birth")
            }
        }

        binding.edtIdno.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isNullOrEmpty()){
                    if (viewModel.nationalityCountryCode.value.equals("Malaysia",false)) {
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

    private fun setCurrentCountry(){
        viewModel.birthPlaceCountryCode.value = getCurrentCountryName()
        viewModel.nationalityCountryCode.value = getCurrentCountryName()

        viewModel.birthPlaceCountryFlag.value = World.getCountryFrom(getCurrentCountryName()).flagResource
        viewModel.nationalityCountryFlag.value = World.getCountryFrom(getCurrentCountryName()).flagResource

        if (!viewModel.nationalityCountryCode.value.isNullOrEmpty()){
            if (viewModel.nationalityCountryCode.value.equals("Malaysia",false)){
                viewModel.patientIdNoCharLength.set(12)
            }else{
                viewModel.patientIdNoCharLength.set(15)
            }
        }

        /*binding.imgCountryFlagPob.setImageResource(World.getCountryFrom(getCurrentCountryName()).flagResource)
        binding.imgCountryFlagNationlity.setImageResource(World.getCountryFrom(getCurrentCountryName()).flagResource)*/

        /*viewModel.birthPlaceCountryCode.set(World.getCountryFrom(getCurrentCountryName()).alpha3)
        viewModel.nationalityCountryCode.set(World.getCountryFrom(getCurrentCountryName()).alpha3)*/
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
                        //viewModel.setCurrentCountry(getCurrentCountryName()!!)
                        setCurrentCountry()
                        //toast(getCurrentCountryName()!!)
                    } else {
                        toast("Permission Denied, You cannot get Location")
                    }
                }
            }
        }
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
        viewModel.idNo.value = ""
        if (countryCode == "MYS"){
            viewModel.patientIdNoCharLength.set(12)
        }else{
            viewModel.patientIdNoCharLength.set(15)
        }
        //binding.imgCountryFlagNationlity.setImageResource(World.getFlagOf(countryCode))

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
        hideKeyBoard()
        Paper.book().write(Passparams.ADD_DEPENDENT_SUCCESS,true)
        finish()
        //DependentDialogFragment().show(supportFragmentManager,"Add")
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        hideKeyBoard()
    }

    override fun onResume() {
        super.onResume()
        binding.edtQrCode.setText("")
        qrCodeValue = Paper.book().read(Passparams.QR_CODE_VALUE,"")
        if (!qrCodeValue.isNullOrEmpty()){
            viewModel.existingUserprivateKey.set(decryptQRValue(qrCodeValue,changeDateFormatForPrivateKeyDecrypt(binding.edtDob.text.toString())!!))
            binding.edtQrCode.setText(qrCodeValue)
        }
    }
}