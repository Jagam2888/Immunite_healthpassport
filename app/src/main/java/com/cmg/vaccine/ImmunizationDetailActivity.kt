package com.cmg.vaccine

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityImmunizationDetailBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ImmunizationDetailActivity : BaseActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:ActivityImmunizationDetailBinding
    private lateinit var viewModel:WorldEntryViewModel

    private val factory:WorldEntryViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_immunization_detail)
        viewModel = ViewModelProvider(this,factory).get(WorldEntryViewModel::class.java)
        binding.viewmodel = viewModel

        val vaccineCode = intent.extras?.getString(Passparams.VACCINE_CODE,"")
        viewModel.getVaccineDetail(vaccineCode!!)

        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}