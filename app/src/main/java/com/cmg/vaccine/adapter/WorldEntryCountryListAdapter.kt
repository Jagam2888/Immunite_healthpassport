package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.cmg.vaccine.R
import com.cmg.vaccine.database.WorldEntryCountries
import com.cmg.vaccine.databinding.CountryListItemBinding
import com.cmg.vaccine.databinding.WorldEntryCountryListItemBinding
import com.cmg.vaccine.util.getTwoAlpha
import com.jdev.countryutil.Country

class WorldEntryCountryListAdapter(
    private var countryList: ArrayList<WorldEntryCountries>
):RecyclerView.Adapter<WorldEntryCountryListAdapter.MyViewHolder>(),Filterable {

    private val countryListFull:ArrayList<WorldEntryCountries> = ArrayList()

    init {
        countryListFull.addAll(countryList)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.world_entry_country_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.worldEntryCountryListItemBinding.countries = countryList[position]

        val flag = Country.getCountryByISO(getTwoAlpha(countryList[position].countryCodeAlpha!!)).flag
        //val flag = World.getFlagOf(countryList.get(position).countryCodeAlpha)
        holder.worldEntryCountryListItemBinding.imgFlag.setImageResource(flag)

    }

    override fun getItemCount() = countryList.size

    override fun getFilter(): Filter {
        return filterList()
    }

    private fun filterList() = object :Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            var tempList:ArrayList<WorldEntryCountries> = ArrayList()
            if (constraint.isNullOrEmpty()){
                tempList.clear()
                tempList.addAll(countryListFull)
            }else{
                var value = constraint.toString().toLowerCase().trim()
                tempList.clear()
                countryListFull.forEach {
                    if (it.countryName?.toLowerCase()?.startsWith(value) == true){
                        tempList.add(it)
                    }
                }
            }
            val filterList = FilterResults()
            filterList.values = tempList
            return filterList
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            countryList.clear()
            countryList.addAll(results?.values as List<WorldEntryCountries>)
            notifyDataSetChanged()
        }
    }

    inner class MyViewHolder(
        val worldEntryCountryListItemBinding: WorldEntryCountryListItemBinding
    ):RecyclerView.ViewHolder(worldEntryCountryListItemBinding.root)
}