package com.cmg.vaccine

import android.os.Bundle
import android.view.View

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.adapter.CountryListAdapter
import com.cmg.vaccine.databinding.ActivityAddWorldEntryBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.util.hideKeyBoard
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class AddWorldEntryActivity : BaseActivity(),KodeinAware,SimpleListener {
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
        //binding.lifecycleOwner = this
        viewModel.listener = this


        viewModel.countryList.observe(this, Observer {
            countryListAdapter = CountryListAdapter(it)
            binding.recyclerViewCountries.also {
                it.layoutManager = LinearLayoutManager(this)
                it.adapter = countryListAdapter
            }
        })

        binding.txtClose.setOnClickListener {
            hideKeyBoard()
            finish()
        }

        binding.recyclerViewCountries.addOnItemTouchListener(RecyclerViewTouchListener(this,binding.recyclerViewCountries,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                viewModel.insertWorldEntry(viewModel.countryList.value?.get(position)?.countryName!!)
                hideKeyBoard()
                finish()
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))

        binding.searchview.setOnQueryTextListener(object :android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryListAdapter?.filter?.filter(newText)
                return false
            }
        })

    }

    override fun onStarted() {
    }

    override fun onSuccess(msg: String) {
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}