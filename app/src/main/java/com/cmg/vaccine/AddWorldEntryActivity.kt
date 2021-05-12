package com.cmg.vaccine

import android.os.Bundle
import android.view.View

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.adapter.WorldEntryCountryListAdapter
import com.cmg.vaccine.databinding.ActivityAddWorldEntryBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import io.paperdb.Paper
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class AddWorldEntryActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityAddWorldEntryBinding
    private lateinit var viewModel:WorldEntryViewModel

    private val factory:WorldEntryViewModelFactory by instance()
    var worldEntryCountryListAdapter:WorldEntryCountryListAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_world_entry)
        viewModel = ViewModelProvider(this,factory).get(WorldEntryViewModel::class.java)
        binding.viewModel = viewModel
        //binding.lifecycleOwner = this
        viewModel.listener = this

        viewModel.loadWorldEntriesData()
        viewModel.loadWorldEntryCountries()



        viewModel.countryList.observe(this, Observer {
            worldEntryCountryListAdapter = WorldEntryCountryListAdapter(it)
            binding.recyclerViewCountries.also {
                it.layoutManager = LinearLayoutManager(this)
                it.adapter = worldEntryCountryListAdapter
            }
        })

        binding.txtClose.setOnClickListener {
            hideKeyBoard()
            finish()
        }

        binding.recyclerViewCountries.addOnItemTouchListener(RecyclerViewTouchListener(this,binding.recyclerViewCountries,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                viewModel.insertWorldEntry(viewModel.countryList.value?.get(position)?.countryName!!,viewModel.countryList.value?.get(position)?.countryCodeAlpha!!)
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
                worldEntryCountryListAdapter?.filter?.filter(newText)
                return false
            }
        })

    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)

        if (msg.startsWith("1")){
            Paper.book().write(Passparams.ERROR_ALERT,true)
        }else if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialog(resources.getString(R.string.failed), showMsg, false, supportFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(msg, "", false, supportFragmentManager)
        }
    }
}