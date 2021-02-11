package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.HomeListTestItemBinding
import com.cmg.vaccine.model.DashboardTestData

class HomeTestListAdapter(
    private val list:List<DashboardTestData>
):RecyclerView.Adapter<HomeTestListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.home_list_test_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.homeListTestItemBinding.model = list.get(position)
        if (position == 0){
            holder.homeListTestItemBinding.btnSucess.setImageResource(R.drawable.success_icon_green)
            holder.homeListTestItemBinding.txtLatestRecords.visibility = View.VISIBLE
            holder.homeListTestItemBinding.mainlayoutDashboardList.setBackgroundResource(R.drawable.box_green_outline)
            holder.homeListTestItemBinding.txtVaccinedate.setBackgroundResource(R.drawable.home_list_injected_green)

        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
        val homeListTestItemBinding: HomeListTestItemBinding
    ):RecyclerView.ViewHolder(homeListTestItemBinding.root)
}