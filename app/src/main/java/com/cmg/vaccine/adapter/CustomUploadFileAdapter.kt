package com.cmg.vaccine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import com.cmg.vaccine.R
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.listener.AdapterListener

class CustomUploadFileAdapter(
    private val context: Context,
    private val list: ArrayList<String>
):BaseAdapter() {

    private val mInflator: LayoutInflater = LayoutInflater.from(context)
    var listener:AdapterListener?=null

    override fun getCount() = list.size

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.upload_document_view, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        vh.editText.setText(list[position])
        vh.editText.setOnSingleClickListener{
            listener?.onClick(position)
        }
        return view
    }

    private class ViewHolder(row: View?) {
        public val editText: EditText = row?.findViewById<EditText>(R.id.edittext)!!
    }

    fun refreshList(listNew: ArrayList<String>){
        val listsss = list.size
        list.clear()
        list.addAll(listNew)
        notifyDataSetChanged()
    }
}