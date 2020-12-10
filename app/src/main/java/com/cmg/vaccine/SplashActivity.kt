package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivitySplashBinding
import com.cmg.vaccine.viewmodel.AuthViewModel
import com.cmg.vaccine.viewmodel.AuthViewModelFactory
import com.cmg.vaccine.viewmodel.SplashModelFactory
import com.cmg.vaccine.viewmodel.SplashViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(),KodeinAware{

    override val kodein by kodein()
    private val factory:SplashModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivitySplashBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        val viewModel : SplashViewModel = ViewModelProvider(this,factory).get(SplashViewModel::class.java)

        binding.splashviewmodel = viewModel

        viewModel.getURL()?.observe(this, Observer {
            if (it != null){
                Intent(this,LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                }
            }
        })
    }
}