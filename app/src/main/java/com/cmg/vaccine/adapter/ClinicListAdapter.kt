package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.ClinicListItemBinding
import com.cmg.vaccine.model.response.ClinicListResponseDataOne

class ClinicListAdapter(
):RecyclerView.Adapter<ClinicListAdapter.MyViewHolder>() {

    var list = listOf<ClinicListResponseDataOne>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
            DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.clinic_list_item,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.clinicListItemBinding.data = list[position]
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
            val clinicListItemBinding: ClinicListItemBinding
    ):RecyclerView.ViewHolder(clinicListItemBinding.root)

}