package com.cmg.vaccine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.FeedbackListItemBinding
import com.cmg.vaccine.model.response.GetFeedbackStatusResponseData
import com.cmg.vaccine.util.changeDateFormatFeedback
import com.cmg.vaccine.viewmodel.FeedBackViewModel

class FeedBackListAdapter(
    private val context: Context,
    private val viewModel: FeedBackViewModel
):RecyclerView.Adapter<FeedBackListAdapter.MyViewHolder>() {


    var list = listOf<GetFeedbackStatusResponseData>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.feedback_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.feedbackListItemBinding.feedback = list[position]
        val adapter = FeedBackAttachmentAdapter(context)
        holder.feedbackListItemBinding.adapter = adapter

        holder.feedbackListItemBinding.imageRecyclerview.apply {
            val customLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            layoutManager = customLayoutManager
        }

        if (!list[position].caseNo.isNullOrEmpty()) {
            val attachementList = viewModel.getAttachementList(list[position].caseNo)
            if (!attachementList.isNullOrEmpty()) {
                adapter.list = attachementList
            }
        }

        holder.feedbackListItemBinding.ratingBar.count = list[position].rating

        if (!list[position].createdDate.isNullOrEmpty()){
            holder.feedbackListItemBinding.feedbackDate.text = changeDateFormatFeedback(list[position].createdDate)
        }

        if (!viewModel.userProfileList.value.isNullOrEmpty()) {
            viewModel.userProfileList.value?.forEach {
                if (list[position].caseSubId == it.subId){
                    holder.feedbackListItemBinding.userName.text = it.userName
                    holder.feedbackListItemBinding.userRelationship.text = it.profile+" Account"
                }
            }
        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
        val feedbackListItemBinding: FeedbackListItemBinding
    ):RecyclerView.ViewHolder(feedbackListItemBinding.root)
}