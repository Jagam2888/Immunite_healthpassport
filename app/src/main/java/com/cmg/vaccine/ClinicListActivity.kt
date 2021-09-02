package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.BaseActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.adapter.ClinicListAdapter
import com.cmg.vaccine.databinding.ActivityClinicListBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.viewmodel.ClinicListViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ClinicListViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ClinicListActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityClinicListBinding
    private lateinit var viewModel:ClinicListViewModel
    private lateinit var clicnicAdapter:ClinicListAdapter

    private val factory:ClinicListViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_clinic_list)

        viewModel = ViewModelProvider(this,factory).get(ClinicListViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        clicnicAdapter = ClinicListAdapter()
        binding.adapter = clicnicAdapter
        viewObserver()

        binding.imgBack.setOnClickListener {finish()}
    }

    private fun viewObserver(){
        viewModel.clincList.observe(this,{
            clicnicAdapter.list = it
        })
    }

    override fun onStarted() {
        show(binding.clinicProgressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.clinicProgressBar)
    }

    override fun onFailure(msg: String) {
        hide(binding.clinicProgressBar)
    }

    override fun onShowToast(msg: String) {
        hide(binding.clinicProgressBar)
    }
}