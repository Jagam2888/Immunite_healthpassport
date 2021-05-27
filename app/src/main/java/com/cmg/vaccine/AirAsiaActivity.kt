package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityAirAsiaBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.AirasiaViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.AirasiaViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AirAsiaActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityAirAsiaBinding
    private lateinit var viewModel:AirasiaViewModel

    private val factory:AirasiaViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_air_asia)
        viewModel = ViewModelProvider(this,factory).get(AirasiaViewModel::class.java)
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this
        viewModel.listener = this

        binding.imgBack.setOnClickListener {
            hideKeyBoard()
            finish()
        }
    }

    override fun onStarted() {
        show(binding.progressAirasia)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressAirasia)
        showAlertDialogWithClick(resources.getString(R.string.success), msg, true, true,supportFragmentManager)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressAirasia)
        if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialog(resources.getString(R.string.failed), showMsg, false, supportFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(resources.getString(R.string.failed), msg, false, supportFragmentManager)
        }
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressAirasia)
        toast(msg)
    }
}