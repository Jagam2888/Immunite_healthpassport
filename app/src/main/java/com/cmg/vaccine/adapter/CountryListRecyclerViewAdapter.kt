package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SectionIndexer
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.Country
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.CountryListItemBinding
import com.cmg.vaccine.listener.AddSelectedRVListener

class CountryListRecyclerViewAdapter(
    private val countryList: ArrayList<Country>,
    var addSelectedRVListener: AddSelectedRVListener?,
    var type:String?

): RecyclerView.Adapter<CountryListRecyclerViewAdapter.MyViewHolder>(), Filterable, SectionIndexer {

    private var mSectionPositions= ArrayList<Int>()

    private val countryListFull:ArrayList<Country> = ArrayList()
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
        holder.countryListItemBinding.countries = countryList[position]

        holder.countryListItemBinding.countryListItemLayout.setOnClickListener {
            //addSelectedRVListener?.setClick(countryList[position].name, countryList[position].alpha2)
            addSelectedRVListener?.onClickSelectedItem(countryList[position].name,
                countryList[position].alpha3,type.toString())
        }
        //val flag = World.getFlagOf(countryList.get(position).countryCodeAlpha)
        //holder.binding.imgFlag.setImageResource(flag)

    }

    override fun getItemCount(): Int {
        return countryList.size
    }


    override fun getSections(): Array<out Any>? {
        val sections: MutableList<String> = ArrayList()
        mSectionPositions = ArrayList()
        var i = 0
        val size: Int = countryList.size
        while (i < size) {
            val section: String =
                java.lang.String.valueOf((countryList.get(i).name.toString()).first()).toUpperCase()
            if (!sections.contains(section)) {
                sections.add(section)
                mSectionPositions.add(i)
            }
            i++
        }

        return sections.toTypedArray()
    }

    override fun getPositionForSection(i: Int): Int {
        return mSectionPositions.get(i)
    }

    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    override fun getFilter(): Filter {
        return filterList()
    }

    private fun filterList() = object : Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            var tempList:ArrayList<Country> = ArrayList()
            if (constraint.isNullOrEmpty()){
                tempList.clear()
                tempList.addAll(countryListFull)
            }else{
                var value = constraint.toString().toLowerCase().trim()
                tempList.clear()
                countryListFull.forEach {
                    if (it.name?.toLowerCase()?.startsWith(value) == true){
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
            countryList.addAll(results?.values as List<Country>)
            notifyDataSetChanged()
        }
    }

    inner class MyViewHolder(
        val countryListItemBinding: CountryListItemBinding
    ):RecyclerView.ViewHolder(countryListItemBinding.root)

}