package com.cmg.vaccine

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityWelcomeBinding
import com.cmg.vaccine.DialogFragment.WelcomeDialogFragment
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.checkGoogleServices
import com.cmg.vaccine.util.showAlertDialog
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.WelcomeViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WelcomeViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class WelcomeActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityWelcomeBinding
    private lateinit var viewModel:WelcomeViewModel
    var lastClickSignup:Long = 0
    var lastClickExisting:Long = 0

    private val factory:WelcomeViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome)
        viewModel = ViewModelProvider(this,factory).get(WelcomeViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        WelcomeDialogFragment().show(supportFragmentManager,"Welcome")

        binding.btnNewUser.setOnSingleClickListener {
            Intent(this,SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnExistingUser.setOnSingleClickListener {
            Intent(this,ExistingUserActivity::class.java).also {
                startActivity(it)
            }
        }

        /*binding.btnNewUser.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickSignup<1000){
                return@setOnClickListener
            }
            lastClickSignup = SystemClock.elapsedRealtime()
            Intent(this,SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnExistingUser.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickExisting<1000){
                return@setOnClickListener
            }
            lastClickExisting = SystemClock.elapsedRealtime()
            Intent(this,ExistingUserActivity::class.java).also {
                startActivity(it)
            }
        }*/



    }

    override fun onStarted() {
    }

    override fun onSuccess(msg: String) {
    }

    override fun onFailure(msg: String) {
        if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }
    }

    override fun onShowToast(msg: String) {
        toast(msg)
    }
}