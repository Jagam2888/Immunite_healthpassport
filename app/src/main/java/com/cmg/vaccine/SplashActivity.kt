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

        Log.d("decrypt_splash",
            decryptKey("uVavFdRLVIG5hMxlyAhr\\/mQTV4v8\\/htWDwnQoP7YYwtRtpPzP2HxsFwtvQY\\/2GuJVlxcs7ckrjvC\n9WN4q5lcNRzVYq6ZV7i16P9fHe6S1RQP3+qrXzsA6MAw\\/3DfsVZuH8bCMLSssAgCBYRenDedlef9\nHPQ0wDfUUrPqap3OXDgjZ7+SDrQ7HB4yt0r5\\/1IquUAtbPrSwo7pOOuUqq7RoWGTEDau+jpvQijZ\n2ot2sdp4OOicgcPWQYeuA8QyWEkhEfHVd43J5Zr5lIGoUm3NwAFarNuDmg9dDoY4gVpg1Xtntdz+\nWG38KaVl5bMwtp5joEcqcmnDY3X7An4hkjbLlQ==","20210327")!!)

        Log.d("timestamp", calculateHours(changeDateToTimeStamp("2021-03-27T19:55:00+0800")!!,System.currentTimeMillis()).toString())

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

    fun decryptKey(key:String,dob:String):String?{
        return EncryptionUtils.decryptBackupKey(key,dob)
    }
}