package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityEditDependentProfileBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.DependentViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.DependentViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class EditDependentProfileActivity : AppCompatActivity(),KodeinAware,SimpleListener {

    override val kodein by kodein()
    private lateinit var binding:ActivityEditDependentProfileBinding
    private lateinit var viewModel: DependentViewModel

    private val factory: DependentViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_dependent_profile)
        viewModel = ViewModelProvider(this,factory).get(DependentViewModel::class.java)
        binding.dependentViewModel = viewModel

        binding.lifecycleOwner = this
        viewModel.listener = this

        val childPrivateKey = intent.extras?.getString("child_private_key","")
        viewModel.loadProfileData(childPrivateKey!!)
    }

    override fun onStarted() {
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        finish()
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}