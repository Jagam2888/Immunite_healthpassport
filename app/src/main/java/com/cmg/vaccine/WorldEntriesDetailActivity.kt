package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.adapter.WorldEntriesDetailAdapter
import com.cmg.vaccine.data.WorldEntriesListData.WorldCountryData
import com.cmg.vaccine.databinding.ActivityWorldEntriesDetailBinding
import kotlinx.android.synthetic.main.layout_travel.*

class WorldEntriesDetailActivity : BaseActivity() {

    private lateinit var binding:ActivityWorldEntriesDetailBinding

    private var adapters: WorldEntriesDetailAdapter? = null
    private var titleList: List<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_world_entries_detail)

        binding.closeBtn.setOnClickListener {
            finish()
        }

        importData()
    }

    fun importData()
    {
        val listData =WorldCountryData
        titleList = ArrayList(listData.keys)
        adapters = WorldEntriesDetailAdapter(this, titleList as ArrayList<String>, listData)
        listview_travel!!.setAdapter(adapters)
    }

}