package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.SplashRepositary
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.calculateHours
import com.cmg.vaccine.util.changeDateToTimeStamp
import com.cmg.vaccine.viewmodel.SplashViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SplashModelFactory
import immuniteeEncryption.EncryptionUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class SplashActivity() : BaseActivity(),KodeinAware{
    override val kodein by kodein()
    private lateinit var viewModel:SplashViewModel

    private val factory:SplashModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this,factory).get(SplashViewModel::class.java)

        viewModel.initSplashScreen()
        viewModel.liveData.observe(this, Observer {
            when(it){
                is SplashViewModel.SplashState.MainActivity -> {
                    navigateActivity()
                }
            }
        })

        //Log.d("decrypt_splash",decryptKey("uVavFdRLVIG5hMxlyAhr\/mQTV4v8\/htWDwnQoP7YYwsF56IBKnGSOea\/g1maoRypMcB7kMMdf9OC\ntpVTlPhIomqVcXtIGaJ6ASNH1dK+ApeVWWg400Pbx2lHRP0zZmA4LgIhWyU7\/AUHxVvmM8rxYqxw\nmSFi8ef2TE2pkH+a+SRgR5N\/TExlCWUyV7Ufj06teFTHX3lgRVKN8v8jIGNT6z3gx3nj9I7QnH9c\nSGCuFrqhIV7OhCj\/4Xz\/UP2H6\/RehBnfEJWUbxqoKvuTf0kuJwBsU3KstzKY09h1jGJ7Ut2wLR\/P\nOQ6tu7JiCJJWuajAqcB0vpPgxseY\/HCnNC1ZzqMM4JThoOfv94D1Vjyt29A=","20210327")!!)


    }
    private fun navigateActivity(){
        if (viewModel.subId.value.isNullOrEmpty()) {
            Intent(this, IntroActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }else{
            if (viewModel.userData.value?.virifyStatus == "Y"){
                if (viewModel.loginPin != null) {
                    if (viewModel.loginPin.value?.enable == "Y") {
                        Intent(this, LoginPinActivity::class.java).also {
                            it.putExtra(Passparams.ISCREATE,"")
                            startActivity(it)
                            finish()
                        }
                    } else {
                        Intent(this, MainActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }

                    }
                }else{
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }else{
                Intent(this, IntroActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    fun decryptKey(key:String,dob:String):String?{
        return EncryptionUtils.decryptBackupKey(key,dob)
    }
}