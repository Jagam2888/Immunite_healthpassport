package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityEcodeValidationBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.showAlertDialogWithClick
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.ECodeValidationViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ECodeValidationViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ECodeValidationActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityEcodeValidationBinding
    private lateinit var viewModel:ECodeValidationViewModel

    private val factory:ECodeValidationViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ecode_validation)
        viewModel = ViewModelProvider(this,factory).get(ECodeValidationViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.listener = this


        binding.imgBack.setOnSingleClickListener{
            finish()
        }

        val eCode = intent.extras?.getString("ecode","")
        viewModel.eCode.value = eCode
    }

    override fun onStarted() {
        show(binding.ecodeProgress)
    }

    override fun onSuccess(msg: String) {
        hide(binding.ecodeProgress)
        showAlertDialogWithClick(resources.getString(R.string.success), msg, true, true,supportFragmentManager)

    }

    override fun onFailure(msg: String) {
        hide(binding.ecodeProgress)
        if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialogWithClick(resources.getString(R.string.failed), showMsg, false,true, supportFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialogWithClick(showMsg, resources.getString(R.string.check_internet), false,true, supportFragmentManager)
        }else {
            showAlertDialogWithClick(resources.getString(R.string.failed), msg, false, true,supportFragmentManager)
        }
    }

    override fun onShowToast(msg: String) {
        hide(binding.ecodeProgress)
        toast(msg)
    }
}