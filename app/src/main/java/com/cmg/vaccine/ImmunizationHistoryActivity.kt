package com.cmg.vaccine

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.adapter.CustomUploadFileAdapter
import com.cmg.vaccine.data.MultipleFilesData
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityImmunizationHistoryBinding
import com.cmg.vaccine.listener.AdapterListener
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.ImmunizationHistoryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ImmunizationHistoryViewModelFactory
import com.niwattep.materialslidedatepicker.SlideDatePickerDialogCallback
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ImmunizationHistoryActivity : BaseActivity(),KodeinAware,SimpleListener,AdapterListener,SlideDatePickerDialogCallback{
    override val kodein by kodein()


    private lateinit var binding:ActivityImmunizationHistoryBinding
    private lateinit var viewModel:ImmunizationHistoryViewModel

    private val immunizationHistoryViewModelFactory:ImmunizationHistoryViewModelFactory by instance()
    var lastClickTimeDOB:Long = 0
    var lastClickTimeDOBTime:Long = 0
    var clickPosition:Int = 0
    private var listOfFiles:ArrayList<MultipleFilesData>?=null
    private lateinit var customAdapter:CustomUploadFileAdapter

    companion object{
        const val READ_EXTERNAL = 101
        const val PICK_PDF_FILE = 102
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_immunization_history)

        viewModel = ViewModelProvider(this, immunizationHistoryViewModelFactory).get(ImmunizationHistoryViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.userSubId.value = intent.extras?.getString(Passparams.SUBSID,"")

        /*binding.btnNfc.setOnSingleClickListener() {
            if (checkPermission()){
                openPDFFile()
            }else{
                requestPermission()
            }
        }*/



        viewModel.listener = this
        viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode)

        binding.edtDob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateDateFormat(binding.edtDob.text.toString())) and (binding.edtDob.text?.isNotEmpty() == true)) {
                    binding.edtDob.error = "Sorry! Invalid Date"
                } else {
                    binding.edtDob.error = null
                }
            }
        })

        binding.edtDobTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((!validateTime(binding.edtDobTime.text.toString())) and (binding.edtDobTime.text?.isNotEmpty() == true)) {
                    binding.edtDobTime.error = "Sorry! Invalid Time"
                } else {
                    binding.edtDobTime.error = null
                }
            }
        })

        binding.btnDobCalender.setOnSingleClickListener {
            hideKeyBoard()
            showSliderDatePickerDialog("DOB",supportFragmentManager,
                Calendar.getInstance().apply {
                    set(Calendar.YEAR,1900)
                }, Calendar.getInstance())
            //showDatePickerDialog(binding.edtDob)
        }

        binding.btnDobTimeCalender.setOnSingleClickListener() {
            hideKeyBoard()
            showTimepickerDialog(binding.edtDobTime, "1200")
        }


        //binding.ccpLoadCountryCode.registerCarrierNumberEditText(binding.edtMobile)

        binding.ccpLoadCountryCode.setOnCountryChangeListener {
            viewModel.selectedItemContactCode.set(binding.ccpLoadCountryCode.selectedCountryCode) }

        binding.imgBack.setOnClickListener {
            finish()
        }



        listOfFiles = ArrayList<MultipleFilesData>()

        listOfFiles!!.add(MultipleFilesData(
            "",""
        ))




        customAdapter = CustomUploadFileAdapter(this,listOfFiles!!)
        binding.listview.adapter = customAdapter
        customAdapter.listener = this


        binding.addDocument.setOnSingleClickListener{
            if (listOfFiles!!.size < 5) {

                listOfFiles!!.add(MultipleFilesData(
                    "",""
                ))
                Log.d("list_size_after",listOfFiles!!.size.toString())
                customAdapter.notifyDataSetChanged()
            }else{
                toast("Sorry, Already you have reached your attachment limit")
            }
        }

    }

    private fun openPDFFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_FILE)
    }

    private fun checkPermission():Boolean{
        return ((ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED))
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == PICK_PDF_FILE) and (resultCode == Activity.RESULT_OK)){
            if (data == null)
                return
            try {
                val uri = data.data
                val filePath = getFileFromUri(uri!!)
                if (getFIleSize(filePath) <= 5) {
                    viewModel.filePath.value = filePath.absolutePath
                    viewModel.fileName.value = filePath.name

                    val multipleFilesData = MultipleFilesData(
                        filePath.name,filePath.absolutePath
                    )
                    listOfFiles!![clickPosition] = multipleFilesData
                    customAdapter.notifyDataSetChanged()
                    viewModel._filePathList.value = listOfFiles

                }
            }catch (e:Exception){
                e.printStackTrace()
                toast("Sorry!,this folder file can't access")
            }


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            READ_EXTERNAL -> {
                if (grantResults.isNotEmpty()) {
                    val locationAccepted: Boolean =
                            grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (locationAccepted) {
                        openPDFFile()
                    } else {
                        toast("Permission Denied, You cannot get Location")
                    }
                }
            }
        }
    }

    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        binding.edtDob.setText(SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
        binding.edtDob.setSelection(binding.edtDob.length())
    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        Intent(this, SuccessPopUpActivity::class.java).also {
            it.putExtra(Passparams.NAVIGATE_FROM,Passparams.IMMUNIZATIONHISTORY)
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

    override fun onClick(position: Int) {
        if (checkPermission()){
            clickPosition = position
            openPDFFile()
        }else{
            requestPermission()
        }
    }
}