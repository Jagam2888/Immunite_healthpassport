package com.cmg.vaccine.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.FileUploadItemBinding
import com.cmg.vaccine.model.response.GetFeedbackStatusResponseAttachment

class FeedBackAttachmentAdapter(
    private val list:List<GetFeedbackStatusResponseAttachment>
):RecyclerView.Adapter<FeedBackAttachmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.file_upload_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if ((!list[position].filePath.isNullOrEmpty()) and (list[position].fileName.contains(".jpg")) or (list[position].fileName.contains(".png"))){
            val uri = Uri.parse(list[position].filePath)
            holder.fileUploadItemBinding.imageView.setImageURI(uri)
        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
        val fileUploadItemBinding: FileUploadItemBinding
    ):RecyclerView.ViewHolder(fileUploadItemBinding.root)
}