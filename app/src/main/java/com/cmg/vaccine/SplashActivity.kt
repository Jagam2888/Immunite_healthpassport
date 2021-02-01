package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.viewmodel.SplashViewModel


class SplashActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModel : SplashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        viewModel.initSplashScreen()
        viewModel.liveData.observe(this, Observer {
            when(it){
                is SplashViewModel.SplashState.MainActivity -> {
                    navigateActivity()
                }
            }
        })

    }
    fun navigateActivity(){
        Intent(this,LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}