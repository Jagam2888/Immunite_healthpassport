package com.cmg.vaccine.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.cmg.vaccine.R
import com.cmg.vaccine.ViewPrivateKeyActivity
import com.cmg.vaccine.model.Dashboard

class MyViewPagerAdapter(
    private val context: Context,
    //private val layouts: IntArray
private val layouts:List<Dashboard>
) : PagerAdapter() {
    private var layoutInflater:LayoutInflater?=null

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view:View = layoutInflater!!.inflate(R.layout.dashboard,container,false)
        val txtName = view.findViewById<TextView>(R.id.txt_name)
        val txtPassportNo = view.findViewById<TextView>(R.id.txt_passport)
        val txtGender = view.findViewById<TextView>(R.id.txt_gender)

        if (!layouts.get(position).relationShip.isNullOrEmpty()){
            val linearLayout = view.findViewById<LinearLayout>(R.id.layout_relationShip)
            linearLayout.visibility = View.VISIBLE
            val txtRelationShip = view.findViewById<TextView>(R.id.txt_relationship)
            txtRelationShip.text = layouts.get(position).relationShip
        }

        txtName.text = layouts.get(position).fullName
        txtGender.text = layouts.get(position).gender
        txtPassportNo.text = layouts.get(position).passportNo

        val btnPrivateKey = view.findViewById<RelativeLayout>(R.id.btn_view_key)
        btnPrivateKey.setOnClickListener {
            Intent(context, ViewPrivateKeyActivity::class.java).also {
                it.putExtra("private_key",layouts.get(position).privateKey)
                context?.startActivity(it)
            }
        }

        container!!.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

}