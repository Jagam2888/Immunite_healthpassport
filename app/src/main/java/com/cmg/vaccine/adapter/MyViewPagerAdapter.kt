package com.cmg.vaccine.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.cmg.vaccine.R
import com.cmg.vaccine.ViewPrivateKeyActivity
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.util.Passparams

class MyViewPagerAdapter(
    private val context: Context,
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
        val txtRelationShip = view.findViewById<TextView>(R.id.txt_relationship)
        val txtPassportNo = view.findViewById<TextView>(R.id.txt_passport)
        val txtIdNo = view.findViewById<TextView>(R.id.txt_id_no)
        val radioGroup = view.findViewById<RadioGroup>(R.id.dashboard_radio_group)

        val recycler_view = view.findViewById<RecyclerView>(R.id.recycler_view_home)

        //val txtGender = view.findViewById<TextView>(R.id.txt_gender)

        /*if (!layouts.get(position).relationShip.isNullOrEmpty()){
            val linearLayout = view.findViewById<LinearLayout>(R.id.layout_relationShip)
            linearLayout.visibility = View.VISIBLE
            val txtRelationShip = view.findViewById<TextView>(R.id.txt_relationship)
            txtRelationShip.text = layouts.get(position).relationShip
        }*/

        txtName.text = layouts.get(position).fullName
        txtRelationShip.text = layouts.get(position).relationShip+" Account"
        //txtGender.text = layouts.get(position).gender
        txtPassportNo.text = layouts.get(position).passportNo
        txtIdNo.text = layouts.get(position).idNo

        recycler_view.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = HomeVaccineListAdapter(layouts.get(position).data!!)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_vaccine ->{
                    recycler_view.also {
                        it.layoutManager = LinearLayoutManager(context)
                        it.adapter = HomeVaccineListAdapter(layouts.get(position).data!!)
                    }
                    return@setOnCheckedChangeListener
                }
                R.id.radio_test ->{
                    recycler_view.also {
                        it.layoutManager = LinearLayoutManager(context)
                        it.adapter = HomeTestListAdapter(layouts.get(position).dataTest!!)
                    }
                    return@setOnCheckedChangeListener
                }
                R.id.radio_mykey ->{
                    Intent(context,ViewPrivateKeyActivity::class.java).also {
                        it.putExtra(Passparams.PRIVATEKEY,layouts.get(position).privateKey)
                        context.startActivity(it)
                    }
                }
            }
        }
        /*val btnPrivateKey = view.findViewById<RelativeLayout>(R.id.btn_view_key)
        btnPrivateKey.setOnClickListener {
            Intent(context, ViewPrivateKeyActivity::class.java).also {
                it.putExtra("private_key",layouts.get(position).privateKey)
                context?.startActivity(it)
            }
        }*/

        container!!.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

}