package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityChangePasswordBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.isValidPassword
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.ChangePasswordViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ChangePasswordModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ChangePasswordActivity : AppCompatActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityChangePasswordBinding
    private lateinit var viewModel:ChangePasswordViewModel

    private val factory:ChangePasswordModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_password)
        viewModel = ViewModelProvider(this,factory).get(ChangePasswordViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
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
        toast(msg)
        Intent(this,ChangePasswordDoneActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(it)
        }
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}