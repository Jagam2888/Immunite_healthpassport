package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.SubscrptionListItemBinding
import com.cmg.vaccine.model.response.GetSubscriptionPackageData

/**
 * Created by jagad on 8/17/2021
 */
class SubscriptionAdapter:RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder>() {

    var subList = listOf<GetSubscriptionPackageData>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.subscrption_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.subscrptionListItemBinding.data = subList[position]
    }

    override fun getItemCount() = subList.size

    inner class MyViewHolder(
        val subscrptionListItemBinding: SubscrptionListItemBinding
    ):RecyclerView.ViewHolder(subscrptionListItemBinding.root)
}