package com.cmg.vaccine.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.databinding.ListChildItemBinding
import com.cmg.vaccine.model.response.HomeResponse

class ChildListAdapter(
    private val list:List<Dependent>

    ):RecyclerView.Adapter<ChildListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_child_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.childItemBinding.model = list[position]
        if (!list[position].profileImage.isNullOrEmpty()) {
            val uri = Uri.parse(list[position].profileImage)
            holder.childItemBinding.imgProfile.setImageURI(uri)
        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
        val childItemBinding: ListChildItemBinding
    ):RecyclerView.ViewHolder(childItemBinding.root)
}