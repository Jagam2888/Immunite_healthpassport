package com.cmg.vaccine.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.database.Notification
import com.cmg.vaccine.databinding.NotificationListItemBinding
import com.cmg.vaccine.util.changeDateFormatForNotification

class NotificationListAdapter(
    private val notificationList:List<Notification>
):RecyclerView.Adapter<NotificationListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.notification_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.notificationListItemBinding.notification = notificationList[position]
        holder.notificationListItemBinding.txtDate.text = changeDateFormatForNotification(notificationList[position].date!!)
    }

    override fun getItemCount() = notificationList.size

    inner class MyViewHolder(
        val notificationListItemBinding: NotificationListItemBinding
    ):RecyclerView.ViewHolder(notificationListItemBinding.root)
}