package com.cmg.vaccine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.databinding.ActivityAddWorldEntryBinding
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import com.hbb20.CountryCodePicker
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class AddWorldEntryActivity : AppCompatActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:ActivityAddWorldEntryBinding
    private lateinit var viewModel:WorldEntryViewModel

    private val factory:WorldEntryViewModelFactory by instance()
    var countryListAdapter:CountryListAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_world_entry)
        viewModel = ViewModelProvider(this,factory).get(WorldEntryViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.countryList.observe(this, Observer {
            countryListAdapter = CountryListAdapter(it)
            binding.recyclerViewCountries.also {
                it.layoutManager = LinearLayoutManager(this)
                it.adapter = countryListAdapter
            }
        })

        binding.searchview.setOnQueryTextListener(object :android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryListAdapter?.filter?.filter(newText)
                return false
            }
        })







        //val pickerFragment = CountryCodePicker.

        /*supportFragmentManager.beginTransaction()
            .replace(R.id.container, pickerFragment).commit();*/
    }


    /* override fun onSelectCountry(p0: Country?) {
         TODO("Not yet implemented")
     }

     override fun onSelectCurrency(p0: Currency?) {
         TODO("Not yet implemented")
     }*/
}