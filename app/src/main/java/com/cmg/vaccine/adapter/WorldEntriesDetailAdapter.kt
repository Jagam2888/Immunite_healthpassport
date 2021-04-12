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
import com.cmg.vaccine.ImmunizationDetailActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.TestReportDetailActivity
import com.cmg.vaccine.TestReportFailedActivity
import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.util.Passparams
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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
        val indicator = convertView!!.findViewById<ImageView>(R.id.indicator)
        expandedListTextView.text = expandedListText

        if (listPosition == 0){
            indicator.visibility = View.GONE
        }

        if (listPosition ==1){
            val testReportArray = expandedListText.split("|")
            val status:Boolean = testReportArray[1].toBoolean()
            if (!status){
                indicator.setImageResource(R.drawable.ic_red_failed)
            }
            expandedListTextView.text = testReportArray[0]
        }else if (listPosition == 2){
            val vaccineArray = expandedListText.split("|")
            val status:Boolean = vaccineArray[1].toBoolean()
            if (!status){
                indicator.setImageResource(R.drawable.ic_red_failed)
            }
            expandedListTextView.text = vaccineArray[0]
        }
        expandedListTextView.setOnClickListener {

            if (listPosition == 1){
                val testReportArray = expandedListText.split("|")
                val status:Boolean = testReportArray[1].toBoolean()
                if (status){
                    Intent(context, TestReportDetailActivity::class.java).also {
                        it.putExtra(Passparams.TEST_REPORT_ID, testReportArray[2])
                        context.startActivity(it)
                    }
                }else{
                    Intent(context,TestReportFailedActivity::class.java).also {
                        it.putExtra(Passparams.TEST_REPORT_ID,testReportArray[0])
                        context.startActivity(it)
                    }
                }
            }else if (listPosition == 2){
                val vaccineArray = expandedListText.split("|")
                val status:Boolean = vaccineArray[1].toBoolean()
                if (status) {
                    Intent(context, ImmunizationDetailActivity::class.java).also {
                        it.putExtra(Passparams.VACCINE_CODE,vaccineArray[3])
                        context.startActivity(it)
                    }
                }
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
        //val listTitleArray = listTitle.split("_")
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.txt_group)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        val arrow = convertView!!.findViewById<ImageView>(R.id.arrow_img)
        val indicator = convertView!!.findViewById<ImageView>(R.id.indicator)

        if (listPosition == 0){
            indicator.visibility = View.GONE
        }

        /*if (listTitleArray[1].equals("red",true)){
            indicator.setImageResource(R.drawable.red_indicator)
        }else if (listTitleArray[1].equals("green",true)){
            indicator.setImageResource(R.drawable.green_indicator)
        }else if (listTitleArray[1].equals("yellow",true)){
            indicator.setImageResource(R.drawable.yellow_indicator)
        }*/

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
