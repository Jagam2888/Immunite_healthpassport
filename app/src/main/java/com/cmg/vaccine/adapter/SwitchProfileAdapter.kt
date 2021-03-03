package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.SwitchProfileListItemBinding
import com.cmg.vaccine.model.SwitchProfile

class SwitchProfileAdapter(
    private val listUser:List<SwitchProfile>
):RecyclerView.Adapter<SwitchProfileAdapter.MyViewHolder>() {

    private var selectedItem:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.switch_profile_list_item,
            parent,
            false
        )
    )
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.switchProfileListItemBinding.switchprofile = listUser[position]

        holder.switchProfileListItemBinding.checkbox.isChecked = position == selectedItem

        /*holder.switchProfileListItemBinding.container.setOnClickListener {
            changeItem(position)
        }*/
    }

    override fun getItemCount() = listUser.size

    inner class MyViewHolder(
        val switchProfileListItemBinding: SwitchProfileListItemBinding
    ):RecyclerView.ViewHolder(switchProfileListItemBinding.root)

    fun changeItem(position: Int){
        selectedItem = position
        notifyDataSetChanged()
    }
}