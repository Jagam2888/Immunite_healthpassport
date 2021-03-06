package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.repositary.SplashRepositary
import com.cmg.vaccine.util.Passparams
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

        Log.d("decrypt",
            decryptKey("IMMUNITEE|Mi+bsqwIE9WU9eh7yeWkhUNnvNQQ4Wf120lgXlwAjkbAz8TlcafAe2qlVtILlrZoCYPck91Fiim2TPKGL0NVGcrc5rgL6XxYXD1QCoSjjWibOLGxpgueIpD261UJF/0FlgoBXsdsDtYd599y0OTx0LkSqzagTOzEUKFXPxK2+yI=")!!)


    }
    private fun navigateActivity(){
        if (viewModel.subId.value.isNullOrEmpty()) {
            Intent(this, WelcomeActivity::class.java).also {
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
                Intent(this, WelcomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    fun decryptKey(key:String):String?{
        return EncryptionUtils.decryptQrCode(key,5)
    }
}