package com.cmg.vaccine.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cmg.vaccine.R
import com.cmg.vaccine.RRTPCRActivity
import com.cmg.vaccine.VaccineAndTestReportActivity

class WorldEntriesDetailAdapter internal constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: LinkedHashMap<String, List<String>>) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_test_item, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.txt_item)
        expandedListTextView.text = expandedListText
        expandedListTextView.setOnClickListener {
            if(expandedListTextView.text=="Covid-19 Vaccine2 (Pfizer)") {
                var it = Intent(this.context, VaccineAndTestReportActivity::class.java)
                this.context.startActivity(it)
            }
            else{
                var it = Intent(this.context, RRTPCRActivity::class.java)
                this.context.startActivity(it)
            }
        }
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.txt_group)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        val arrow = convertView!!.findViewById<ImageView>(R.id.arrow_img)
        if(isExpanded){
            arrow.setImageResource(R.drawable.arrow_up)
        }else{
            arrow.setImageResource(R.drawable.down_arrow)
        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}