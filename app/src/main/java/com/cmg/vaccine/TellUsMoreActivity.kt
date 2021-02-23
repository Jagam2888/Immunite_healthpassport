package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityTellUsMoreBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.TellUsMoreViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.TellUsViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TellUsMoreActivity : BaseActivity(),KodeinAware,SimpleListener {
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

        /*binding.btnSignup.setOnClickListener {
            Intent(this,OTPVerifyActivity::class.java).also {
                startActivity(it)
            }
        }*/
    }

    override fun onStarted() {
        show(binding.progressTellus)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressTellus)
        toast(msg)
        Intent(this,OTPVerifyActivity::class.java).also {
            it.putExtra("IsExistUser",false)
            it.putExtra(Passparams.SUBSID, viewModel.userSubId.value)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressTellus)
        toast(msg)
    }
}