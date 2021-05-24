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
import com.cmg.vaccine.util.decryptQRValue
import com.cmg.vaccine.viewmodel.SplashViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SplashModelFactory
import com.hbb20.CountryCodePicker

import immuniteeEncryption.EncryptionUtils
import io.paperdb.Paper
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
        val token = Paper.book().read(Passparams.FCM_TOKEN, "")
        Log.d("fcm_token", token)

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
        Log.d("decrypt_splash",
            decryptQRValue("tqo4yEsY0w2nXpMeUeHgGBIcj7uXy271kHN37gfYR8cW/pSVut8/WW8jA6rH/+X5c+SQXglybUBR\nBYzyCilYlevmAVX63YUKO/Vn2UhOb8TxRn5mUE21v69yviCi/4hOsGFrDwWUJEkeuS8DJezVk11c\na2SN5iY4shCNAp5hNX8b7Xq0GgJChdNXffKb8IE9kY8bTgLeYYpP/3mmSmox9GM+9ag8lI3BxJbE\nWn2rRmjZuVFX+L8KyOb+WItbpxW7T8ImGxvpLuRbU8C/2dSOHOqb3y0Qa1YF8vl9PkI3TmrQ7BlT\nsU8mvcrpUaqiqpAJuWOLLR4tZ34fCMhMsqhmXGu5C6y9LCNwjV03L4slygm0qMq7gB5cZHOrPWWY\nPagzD9/qq187AOjAMP9w37FWbmw4Tos8qLG+OKR4N9HJ4jUoE0MPFd5nPO4Mhoz6whE/ZvqaZR21\nTiVyvzwYkqvjHtp4tDfJFBnZQjazsKcP8hY=","20210327")!!)


    }
    private fun navigateActivity(){
        if (viewModel.subId.value.isNullOrEmpty()) {
            Intent(this, IntroActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }else{
            if (viewModel.userData.value?.virifyStatus == "Y"){
                if (viewModel.loginPin != null) {
                    if (viewModel.loginPin.value?.enable == "Y") {
                        Intent(this, LoginPinActivity::class.java).also {
                            it.putExtra(Passparams.ISCREATE,"")
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    } else {
                        Intent(this, MainActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }

                    }
                }else{
                    Intent(this, MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }
            }else{
                Intent(this, IntroActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }
    }

}