package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.databinding.CountryListItemBinding

class CountryListAdapter(
    private var countryList:ArrayList<Countries>
):RecyclerView.Adapter<CountryListAdapter.MyViewHolder>(),Filterable {

    private val countryListFull:ArrayList<Countries> = ArrayList()
    init {
        countryListFull.addAll(countryList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.country_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.countryListItemBinding.countries = countryList.get(position)
    }

    override fun getItemCount() = countryList.size

    override fun getFilter(): Filter {
        return filterList()
    }

    private fun filterList() = object :Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            var tempList:ArrayList<Countries> = ArrayList()
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
            countryList.addAll(results?.values as List<Countries>)
            notifyDataSetChanged()
        }
    }

    inner class MyViewHolder(
        val countryListItemBinding: CountryListItemBinding
    ):RecyclerView.ViewHolder(countryListItemBinding.root)
}