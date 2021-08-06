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
import com.cmg.vaccine.viewmodel.FeedBackViewModel

class FeedBackListAdapter(
    private val context: Context,
    private val list: List<GetFeedbackStatusResponseData>,
    private val viewModel: FeedBackViewModel
):RecyclerView.Adapter<FeedBackListAdapter.MyViewHolder>() {



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

        holder.feedbackListItemBinding.ratingBar.count = list[position].rating

        if (!viewModel.userProfileList.value.isNullOrEmpty()) {
            viewModel.userProfileList.value?.forEach {
                if (list[position].caseSubId == it.subId){
                    holder.feedbackListItemBinding.userName.text = it.userName
                    holder.feedbackListItemBinding.userRelationship.text = it.profile+" Account"
                }
            }
        }


        if (!list[position].caseNo.isNullOrEmpty()) {
            val attachementList = viewModel.getAttachementList(list[position].caseNo)
            if (!attachementList.isNullOrEmpty()) {
                holder.feedbackListItemBinding.imageRecyclerview.also {
                    val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    it.layoutManager = layoutManager
                    it.adapter = FeedBackAttachmentAdapter(attachementList)
                }
            }
        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
        val feedbackListItemBinding: FeedbackListItemBinding
    ):RecyclerView.ViewHolder(feedbackListItemBinding.root)

   /* fun refreshItem(listNew:List<GetFeedbackStatusResponseData>){
        list = listNew
        notifyDataSetChanged()
    }*/
}