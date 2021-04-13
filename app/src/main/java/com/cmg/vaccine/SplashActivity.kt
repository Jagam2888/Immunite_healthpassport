package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.World
import com.cmg.vaccine.repositary.SplashRepositary
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.calculateHours
import com.cmg.vaccine.util.changeDateToTimeStamp
import com.cmg.vaccine.viewmodel.SplashViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SplashModelFactory
import com.hbb20.CountryCodePicker
import com.jdev.countryutil.Country

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


        //Log.d("encrypt_dob",EncryptionUtils.encryptForBackup("823BBF131755B9FF006B73C3D901E9D52C465BD2E0D1E37E53053C91D1912B43","19860206"))
        //Log.d("country_code",CountryCodePicker.)
        //Log.d("decrypt_splash",decryptKey("OU/wqK7aBe7rrDL+JTrUfCnLPuH5GpdgfbjNjbQlF0xqi9CsQp4bOReDIdpmWPWVxn67zy0IQHiQ\nBmBG6Y+8uhebe9FnAXEz7l0gS7d/ezM=","20140808")!!)


    }
    private fun navigateActivity(){
        if (viewModel.subId.value.isNullOrEmpty()) {
            Intent(this, IntroActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
        }else{
            if (viewModel.userData.value?.virifyStatus == "Y"){
                if (viewModel.loginPin != null) {
                    if (viewModel.loginPin.value?.enable == "Y") {
                        Intent(this, LoginPinActivity::class.java).also {
                            it.putExtra(Passparams.ISCREATE,"")
                            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(it)
                        }
                    } else {
                        Intent(this, MainActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(it)
                        }

                    }
                }else{
                    Intent(this, MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(it)
                    }
                }
            }else{
                Intent(this, IntroActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                }
            }
        }
    }

    fun decryptKey(key:String,dob:String):String?{
        return EncryptionUtils.decryptBackupKey(key,dob)
    }
}