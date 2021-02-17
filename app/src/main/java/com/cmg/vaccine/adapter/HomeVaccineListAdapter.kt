package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.DashboardListItemBinding
import com.cmg.vaccine.model.DashboardVaccineData
import com.cmg.vaccine.model.response.VaccineListResponseData

class HomeVaccineListAdapter(
        private val list:List<VaccineListResponseData>
):RecyclerView.Adapter<HomeVaccineListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
            DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.dashboard_list_item,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dashboardListItemBinding.dashboard = list.get(position)
        if (position == 0){
            holder.dashboardListItemBinding.btnSucess.setImageResource(R.drawable.success_icon_green)
            holder.dashboardListItemBinding.txtLatestRecords.visibility = View.VISIBLE
            holder.dashboardListItemBinding.mainlayoutDashboardList.setBackgroundResource(R.drawable.box_green_outline)
            holder.dashboardListItemBinding.txtVaccinedate.setBackgroundResource(R.drawable.home_list_injected_green)

        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
            val dashboardListItemBinding: DashboardListItemBinding
    ):RecyclerView.ViewHolder(dashboardListItemBinding.root)
}