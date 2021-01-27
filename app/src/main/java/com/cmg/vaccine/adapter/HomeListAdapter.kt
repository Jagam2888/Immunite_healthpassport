package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.HomeListItemBinding
import com.cmg.vaccine.model.response.HomeResponse

class HomeListAdapter(
        private val list:List<HomeResponse>
):RecyclerView.Adapter<HomeListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
            DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.home_list_item,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.homeListItemBinding.home = list.get(position)
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
            val homeListItemBinding: HomeListItemBinding
    ):RecyclerView.ViewHolder(homeListItemBinding.root)
}