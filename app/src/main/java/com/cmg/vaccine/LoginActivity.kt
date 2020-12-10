package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityLoginBinding
import com.cmg.vaccine.listener.AuthListener
import com.cmg.vaccine.model.response.AuthResponse
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.repositary.AuthRepositary
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.AuthViewModel
import com.cmg.vaccine.viewmodel.AuthViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(),AuthListener,KodeinAware {

    override val kodein by kodein()
    private val authViewModelFactory:AuthViewModelFactory by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        val viewModel : AuthViewModel = ViewModelProvider(this,authViewModelFactory).get(AuthViewModel::class.java)

        binding.authviewmodel = viewModel

        viewModel.authListener = this

    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(authResponse: AuthResponse) {
        progress_bar.hide()
        toast("Login Successful")
        Log.d("token",authResponse.token.toString())
        Intent(this,MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        progress_bar.hide()
        toast(msg)
    }
}