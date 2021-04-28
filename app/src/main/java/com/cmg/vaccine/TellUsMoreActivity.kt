package com.cmg.vaccine

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.Country
import com.blongho.country_data.World
import com.cmg.vaccine.DialogFragment.CountryListDialogFragment
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityTellUsMoreBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.TellUsMoreViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.TellUsViewModelFactory
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class TellUsMoreActivity : BaseActivity(),KodeinAware,SimpleListener,SlideDatePickerDialogCallback {
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

        binding.checkboxTerms.movementMethod = LinkMovementMethod.getInstance()

        binding.checkboxTerms.setOnSingleClickListener {
            Intent(this,TermsOfUseActivity::class.java).also {
                startActivity(it)
            }
        }

        /*viewModel.countries.observe(this, androidx.lifecycle.Observer {list->
            val arrayList = arrayListOf<Country>()
            arrayList.addAll(list)
            binding.spinnerNationality.adapter = CountryListAdapter(arrayList)
            viewModel.getPlaceOfBirthPos()
        })*/

        viewModel.getPlaceOfBirthPos()

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
            data.putString("from","tell_us")
            myDialogFragment.arguments=data
            myDialogFragment.show(supportFragmentManager,"Place of Birth")

        }*/

        //viewModel.setPlaceOfBirthToNationality()

        /*binding.btnSignup.setOnClickListener {
            Intent(this,OTPVerifyActivity::class.java).also {
                startActivity(it)
            }
        }*/

        binding.btnDateCalender.setOnSingleClickListener {
            hideKeyBoard()
            //showDatePickerDialogForPassport(binding.edtPassportExpDate)
            showSliderDatePickerDialog("passport",supportFragmentManager,
                Calendar.getInstance().apply { add(Calendar.DATE,1) }, Calendar.getInstance().apply { add(Calendar.YEAR,10) })
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

        binding.txtFaceId.setOnSingleClickListener{
            Intent(this,VerifyFaceIDActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        binding.edtPassportExpDate.setText( SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
        binding.edtPassportExpDate.setSelection(binding.edtPassportExpDate.length())
    }

    fun setNation(countryCode:String)
    {
        hideKeyBoard()
        viewModel.nationalityCountryCode.value = World.getCountryFrom(countryCode).name
        viewModel.nationalityCountryFlag.value = World.getFlagOf(countryCode)

        viewModel.idNo.value = ""
        if (countryCode == "MYS"){
            viewModel.patientIdNoCharLength.set(12)
        }else{
            viewModel.patientIdNoCharLength.set(15)
        }

    }

    override fun onStarted() {
        show(binding.progressTellus)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressTellus)
        //toast(msg)
        Intent(this,OTPVerifyActivity::class.java).also {
            it.putExtra(Passparams.NAVIGATE_FROM,Passparams.SIGNUP)
            it.putExtra(Passparams.SUBSID, viewModel.userSubId.value)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressTellus)
        toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressTellus)
        if (msg.startsWith("2")){
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