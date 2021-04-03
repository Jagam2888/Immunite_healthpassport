package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.blongho.country_data.Country
import com.cmg.vaccine.R
import com.cmg.vaccine.database.Countries
import com.cmg.vaccine.databinding.CountryListItemBinding

class CountryListAdapter(
    private val countryList: ArrayList<Countries>
):BaseAdapter(){
    override fun getCount() = countryList.size

    override fun getItem(position: Int) = countryList[position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder:MyViewHolder?=null
        var itemBinding:CountryListItemBinding?=null
        if (convertView == null) {
            itemBinding = DataBindingUtil.inflate<CountryListItemBinding>(
                LayoutInflater.from(parent?.context),
                R.layout.country_list_item,
                parent,
                false
            )
            holder = MyViewHolder(itemBinding!!)
            holder.view = itemBinding.root
            holder.view.tag = holder
        }else{
            holder = convertView.tag as MyViewHolder
        }
        holder.binding.countries = countryList[position]
        return holder.view

    }

    private class MyViewHolder internal constructor(binding: CountryListItemBinding) {
        var view: View = binding.root
        val binding: CountryListItemBinding = binding

    }
}
/*
class CountryListAdapter(
    private val countryList:ArrayList<Country>
):
RecyclerView.Adapter<CountryListAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
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

    inner class MyViewHolder(
    val countryListItemBinding:CountryListItemBinding
    ):RecyclerView.ViewHolder(countryListItemBinding.root)
}*/
