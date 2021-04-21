package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.World
import com.cmg.vaccine.adapter.WorldEntriesDetailAdapter
import com.cmg.vaccine.data.WorldEntriesListData.WorldCountryData
import com.cmg.vaccine.databinding.ActivityWorldEntriesDetailBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import com.jdev.countryutil.Country
import kotlinx.android.synthetic.main.layout_travel.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class WorldEntriesDetailActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityWorldEntriesDetailBinding
    private lateinit var viewModel:WorldEntryViewModel

    private val factory:WorldEntryViewModelFactory by instance()

    private var adapters: WorldEntriesDetailAdapter? = null
    private var titleList: List<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_world_entries_detail)
        viewModel = ViewModelProvider(this,factory).get(WorldEntryViewModel::class.java)
        binding.viewmodel = viewModel

        val countryCode = intent.extras?.getString(Passparams.WORLD_ENTRY_SELECTED_COUNTRY_CODE,"")
        //val countryName = World.getCountryFrom(countryCode).name
        val countryName = Country.getCountryByISO(getTwoAlpha(countryCode!!)).name
        viewModel._selectedCountryName.value = countryName

        viewModel.listener = this

        binding.travel.txtDescription.text = "$countryName has restricted the entry of all foreign nationals who have passed through or have been in China, Iran, Most European Countries, UK, Ireland and Brazil in the past 14 days.`}"

        binding.closeBtn.setOnClickListener {
            finish()
        }

        viewModel.getVaccineAndTestReportList()

            viewModel.loadWorldEntryRulesByCountryData(countryCode!!)
        /*viewModel.countryList.observe(this, Observer {

        })*/


        viewModel.worldEntriesExpandable.observe(this, Observer { listData->
            titleList = ArrayList(listData.keys)
            adapters = WorldEntriesDetailAdapter(this, titleList as ArrayList<String>, listData)
            listview_travel!!.setAdapter(adapters)
        })

        //importData()
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
        if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialog(resources.getString(R.string.failed), showMsg, false, supportFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(msg, "", false, supportFragmentManager)
        }
    }

    private fun importData()
    {
        val listData =WorldCountryData
        titleList = ArrayList(listData.keys)
        adapters = WorldEntriesDetailAdapter(this, titleList as ArrayList<String>, listData)
        listview_travel!!.setAdapter(adapters)
    }

}