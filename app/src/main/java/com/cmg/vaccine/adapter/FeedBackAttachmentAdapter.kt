package com.cmg.vaccine.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.FileUploadItemBinding
import com.cmg.vaccine.model.response.GetFeedbackStatusResponseAttachment
import com.cmg.vaccine.util.Passparams

class FeedBackAttachmentAdapter(
    private val context: Context,
):RecyclerView.Adapter<FeedBackAttachmentAdapter.MyViewHolder>() {

    var list = listOf<GetFeedbackStatusResponseAttachment>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

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
            if (list[position].filePath.startsWith("/data")) {
                val uri = Uri.parse(list[position].filePath)
                holder.fileUploadItemBinding.imageView.setImageURI(uri)
            }else{
                val url = Passparams.FEEDBACK_DOWNLOAD_FILE+"caseNo="+list[position].caseNo+"&fileName="+list[position].fileName
                Glide.with(context)
                    .load(url)
                    .into(holder.fileUploadItemBinding.imageView)
            }
        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(
        val fileUploadItemBinding: FileUploadItemBinding
    ):RecyclerView.ViewHolder(fileUploadItemBinding.root)
}