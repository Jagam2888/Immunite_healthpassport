package com.cmg.vaccine

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityLoginBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.AuthViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.AuthViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.UnsupportedEncodingException
import java.lang.reflect.Array
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

class LoginActivity : BaseActivity(),SimpleListener,KodeinAware {

    override val kodein by kodein()
    private val authViewModelFactory: AuthViewModelFactory by instance()

    private lateinit var binding:ActivityLoginBinding
    private lateinit var viewModel:AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        binding.authviewmodel = viewModel

        viewModel.authListener = this

        binding.newUser.setOnClickListener {
            Intent(this, SignUpActivity::class.java).also {
                startActivity(it)
            }

        }

        binding.forgotPassword.setOnClickListener {
            Intent(this,ForgotPasswordActivity::class.java).also {
                startActivity(it)
            }

        }

    }


    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast("Login Successful")
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }
}
