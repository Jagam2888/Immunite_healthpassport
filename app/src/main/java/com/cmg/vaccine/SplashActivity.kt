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

        Log.d("decrypt_splash",
            decryptKey("IMMUNITEE|7RkWR9YEVpOKxgxDwLTj7/gw3FWl3a/EYzWuNH7wHuecUU7oR/+bjIUDZWjGLfVlAPNjfTaz3uJqEwYmlQypxv35vTloLZRue51fgBYohI5JLW7UqD6JnYiTEANEDdI0W6fW8/yZClAr5NCGlODV4d6NBiYIbamhi8N+Whtjuzc=")!!)


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
        return EncryptionUtils.decryptQrCode(key,1000)
    }
}