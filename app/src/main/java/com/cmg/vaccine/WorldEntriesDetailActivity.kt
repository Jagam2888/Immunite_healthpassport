package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.adapter.WorldEntriesDetailAdapter
import com.cmg.vaccine.data.WorldEntriesListData.WorldCountryData
import com.cmg.vaccine.databinding.ActivityWorldEntriesDetailBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.viewmodel.WorldEntryViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.WorldEntryViewModelFactory
import kotlinx.android.synthetic.main.layout_travel.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class WorldEntriesDetailActivity : BaseActivity(),KodeinAware {
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

        val countryName = intent.extras?.getString(Passparams.WORLD_ENTRY_SELECTED_COUNTRY_NAME,"")
        viewModel._selectedCountryName.value = countryName

        binding.travel.txtDescription.text = "$countryName has restricted the entry of all foreign nationals who have passed through or have been in China, Iran, Most European Countries, UK, Ireland and Brazil in the past 14 days.`}"

        binding.closeBtn.setOnClickListener {
            finish()
        }

        importData()
    }

    private fun importData()
    {
        val listData =WorldCountryData
        titleList = ArrayList(listData.keys)
        adapters = WorldEntriesDetailAdapter(this, titleList as ArrayList<String>, listData)
        listview_travel!!.setAdapter(adapters)
    }

}