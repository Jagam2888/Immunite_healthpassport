package com.cmg.vaccine.adapter

import android.content.Context
import android.database.DatabaseUtils
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.ViewReportListItemBinding
import com.cmg.vaccine.model.response.ViewReport

class ViewReportListAdapter(
        private val viewReportList:List<ViewReport>,
        private val context:Context
) : RecyclerView.Adapter<ViewReportListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.view_report_list_item,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewReportListItemBinding.viewreport = viewReportList.get(position)

        if (viewReportList.get(position).status.equals("EXPIRING")) {
            holder.viewReportListItemBinding.mainLayout.setBackgroundResource(R.drawable.box_red)
            holder.viewReportListItemBinding.txtStatus.setBackgroundColor(ContextCompat.getColor(context,R.color.red))
            holder.viewReportListItemBinding.imgIndicator.setImageResource(R.drawable.btn_red)
        }else if (viewReportList.get(position).status.equals("NEXT")){
            holder.viewReportListItemBinding.mainLayout.setBackgroundResource(R.drawable.box_yellow)
            holder.viewReportListItemBinding.txtStatus.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow))
            holder.viewReportListItemBinding.imgIndicator.setImageResource(R.drawable.btn_yellow)
        }else{
            holder.viewReportListItemBinding.mainLayout.setBackgroundResource(R.drawable.cardview_gray_background)
            holder.viewReportListItemBinding.imgIndicator.setImageResource(R.drawable.success_icon_green)
        }
    }

    override fun getItemCount() = viewReportList.size

    inner class MyViewHolder(
        val viewReportListItemBinding: ViewReportListItemBinding
    ):RecyclerView.ViewHolder(viewReportListItemBinding.root)
}