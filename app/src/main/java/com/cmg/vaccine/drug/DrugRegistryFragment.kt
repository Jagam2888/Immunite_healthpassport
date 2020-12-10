package com.cmg.vaccine.drug

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cmg.vaccine.R

class DrugRegistryFragment : Fragment() {

    companion object {
        fun newInstance() = DrugRegistryFragment()
    }

    private lateinit var viewModel: DrugRegistryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.drug_registry_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DrugRegistryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}