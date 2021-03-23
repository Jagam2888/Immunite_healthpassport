package com.cmg.vaccine

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityExistingUserBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.ExistingUserViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ExistingUserViewModelFactory
import io.paperdb.Paper
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*

class ExistingUserActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityExistingUserBinding
    private lateinit var viewModel:ExistingUserViewModel
    var lastClickTimeDOB:Long = 0
    var lastClickTimeQr:Long = 0
    var qrCodeValue = ""
    var isRestoreFromBackup = false


    private val factory:ExistingUserViewModelFactory by instance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_existing_user)
        viewModel = ViewModelProvider(this,factory).get(ExistingUserViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this


        initViews()
    }


    private fun initViews(){
        Paper.book().write(Passparams.QR_CODE_VALUE,"")
        viewModel.isRestoreForSync.set(false)



        binding.btnDobCalender.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTimeDOB<1000){
                return@setOnClickListener
            }
            lastClickTimeDOB = SystemClock.elapsedRealtime()
            hideKeyBoard()
            showDatePickerDialog(binding.edtDob)
        }

        binding.edtDob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if ((!validateDateFormat(binding.edtDob.text.toString())) and (binding.edtDob.text?.isNotEmpty() == true)){
                    //binding.edtDob.error = "Sorry! Invalid Date of Birth"
                    binding.edtDob.error = "dd/MM/yyyy"
                    viewModel.dobTxt.set("")
                }else{
                    viewModel.dobTxt.set(binding.edtDob.text.toString())
                    binding.edtDob.error = null
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
                    it.putExtra(Passparams.NAVIGATE_TO,Passparams.EXISTING_USER)
                    startActivity(it)
                }
            }else{
                toast("Please enter your Date of Birth")
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_restore ->{
                    isRestoreFromBackup = true
                    viewModel.isRestoreForSync.set(true)
                    viewModel.loadWorldEntries()
                    true
                }
                R.id.radio_manually ->{
                    isRestoreFromBackup = false
                    viewModel.isRestoreForSync.set(false)
                    true
                }
        }
        }

        /*binding.btnSubmit.setOnClickListener {
            Log.d("decrypt_email",decryptQRValue(qrCodeValue, changeDateFormatForPrivateKeyDecrypt(binding.edtDob.text.toString())!!)!!)
        }*/

        /*binding.btnRestore.setOnClickListener {
            viewModel.loadWorldEntries()
        }*/
    }



    override fun onResume() {
        super.onResume()
        //binding.edtQrCode.setText("")
        qrCodeValue = Paper.book().read(Passparams.QR_CODE_VALUE,"")
        if (!qrCodeValue.isNullOrEmpty()){
            viewModel.edttxtQR.set(qrCodeValue)
            qrCodeValue = qrCodeValue.replace("\\n","\n")
            Log.d("private_key",qrCodeValue)
            Log.d("private_key_dob",changeDateFormatForPrivateKeyDecrypt(binding.edtDob.text.toString())!!)
            viewModel.privateKey.set(decryptQRValue(qrCodeValue,changeDateFormatForPrivateKeyDecrypt(binding.edtDob.text.toString())!!))
            //toast(viewModel.privateKey.get()!!)
            //binding.edtQrCode.setText(qrCodeValue)
        }
    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        if (!msg.isNullOrEmpty()) {
            if (msg.equals("restore", true)) {
                Intent(this, RestoredBackupOptionList::class.java).also {
                    it.putExtra(Passparams.USER_DOB, changeDateFormatForPrivateKeyDecrypt(viewModel.dobTxt.get()!!))
                    startActivity(it)
                }
            } else {
                toast(msg)
                Intent(this, SuccessAccountRestoredActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                }
            }
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        if (msg.equals("invalid",true)){
            Intent(this,InvalidPrivateKeyActivity::class.java).also {
                startActivity(it)
            }
        }else {
            toast(msg)
        }
    }
}