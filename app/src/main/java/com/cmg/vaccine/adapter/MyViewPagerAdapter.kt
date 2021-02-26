package com.cmg.vaccine.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.cmg.vaccine.R
import com.cmg.vaccine.TestReportDetailActivity
import com.cmg.vaccine.ViewPrivateKeyActivity
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.google.gson.Gson

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
        val txtNoData = view.findViewById<TextView>(R.id.txt_nodata)
        val radioGroup = view.findViewById<RadioGroup>(R.id.dashboard_radio_group)

        val radioVaccine = view.findViewById<RadioButton>(R.id.radio_vaccine)
        val radioTest = view.findViewById<RadioButton>(R.id.radio_test)

        if (layouts[position].dataTest?.isNotEmpty() == true){
            radioTest.setBackgroundResource(R.drawable.test_data_btn_selector)
        }else{
            radioTest.setBackgroundResource(R.drawable.test_no_data_btn_selector)
        }

        if (layouts[position].data?.isNotEmpty() == true){
            radioVaccine.setBackgroundResource(R.drawable.vaccine_data_btn_selector)
        }else{
            radioVaccine.setBackgroundResource(R.drawable.vaccine_no_data_btn_selector)
        }


        val recyclerViewVaccine = view.findViewById<RecyclerView>(R.id.recycler_view_home_vaccine)
        val recyclerViewTest = view.findViewById<RecyclerView>(R.id.recycler_view_home_test)

        txtName.text = layouts[position].fullName
        txtRelationShip.text = layouts[position].relationShip+" Account"
        txtPassportNo.text = layouts[position].passportNo
        txtIdNo.text = layouts[position].idNo

        if (!layouts[position].data.isNullOrEmpty()) {
            recyclerViewVaccine.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = HomeVaccineListAdapter(layouts[position].data!!)
            }
            if (txtNoData.visibility == View.VISIBLE)
                txtNoData.visibility = View.GONE
        }else{
            if (txtNoData.visibility == View.GONE)
                txtNoData.visibility = View.VISIBLE
        }
        if (!layouts[position].dataTest.isNullOrEmpty()) {
            recyclerViewTest.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = HomeTestListAdapter(layouts[position].dataTest!!)
            }
            if (txtNoData.visibility == View.VISIBLE)
                txtNoData.visibility = View.GONE
        }else{
            if (txtNoData.visibility == View.GONE)
                txtNoData.visibility = View.VISIBLE
        }

        recyclerViewTest.addOnItemTouchListener(RecyclerViewTouchListener(context,recyclerViewTest,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, pos: Int) {
                val gson = Gson()
                val value:String = gson.toJson(layouts[position].dataTest?.get(pos))
                Intent(context,TestReportDetailActivity::class.java).also {
                    it.putExtra(Passparams.TEST_REPORT_ID,value)
                    context.startActivity(it)
                }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))

       /* btnVaccine.setOnClickListener {
            if (recyclerViewVaccine.visibility == View.GONE){
                recyclerViewTest.visibility = View.GONE
                recyclerViewVaccine.visibility = View.VISIBLE
            }
            btnVaccine.setBackgroundResource(R.drawable.vaccine_btn_selected)
            btnTest.setBackgroundResource(R.drawable.test_btn_unselected)
        }

        btnTest.setOnClickListener {
            if (recyclerViewTest.visibility == View.GONE){
                recyclerViewVaccine.visibility = View.GONE
                recyclerViewTest.visibility = View.VISIBLE
            }
            btnTest.setBackgroundResource(R.drawable.test_btn_selected)
            btnVaccine.setBackgroundResource(R.drawable.vaccine_btn_unselected_svg)
        }*/

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_vaccine ->{
                    if (recyclerViewVaccine.visibility == View.GONE){
                        recyclerViewTest.visibility = View.GONE
                        recyclerViewVaccine.visibility = View.VISIBLE
                    }
                    return@setOnCheckedChangeListener
                }
                R.id.radio_test ->{
                    if (recyclerViewTest.visibility == View.GONE){
                        recyclerViewVaccine.visibility = View.GONE
                        recyclerViewTest.visibility = View.VISIBLE
                    }
                    return@setOnCheckedChangeListener
                }
                R.id.radio_mykey ->{
                    radioGroup.check(R.id.radio_vaccine)
                    Intent(context,ViewPrivateKeyActivity::class.java).also {
                        it.putExtra(Passparams.PRIVATEKEY, layouts[position].privateKey)
                        it.putExtra(Passparams.USER_NAME, layouts[position].fullName)
                        it.putExtra(Passparams.RELATIONSHIP, layouts[position].relationShip)
                        it.putExtra(Passparams.SUBSID, layouts[position].subId)
                        context.startActivity(it)
                    }
                }
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