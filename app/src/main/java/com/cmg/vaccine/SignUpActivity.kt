package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivitySignUpBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.isValidEmail
import com.cmg.vaccine.util.isValidPassword
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.SignupViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SignUpModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : AppCompatActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var viewModel:SignupViewModel
    private val signUpModelFactory:SignUpModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this,signUpModelFactory).get(SignupViewModel::class.java)
        binding.signupviewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.listener = this

        /*binding.btnSignup.setOnClickListener {
            Intent(this,TellUsMoreActivity::class.java).also {
                startActivity(it)
            }
        }*/

        binding.txtLogin.setOnClickListener {
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.edtEmail1.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())){
                    binding.edtEmail1.error = "Invalid Email"
                }
            }
        })

        binding.edtEmail2.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())){
                    binding.edtEmail2.error = "Invalid Email"
                }
            }
        })

        binding.edtPassword.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isValidPassword(s.toString()))
                    binding.edtPassword.error = "Password must Minimum 4 character"
            }
        })
    }

    override fun onStarted() {
    }

    override fun onSuccess(msg: String) {
        Intent(this,TellUsMoreActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}