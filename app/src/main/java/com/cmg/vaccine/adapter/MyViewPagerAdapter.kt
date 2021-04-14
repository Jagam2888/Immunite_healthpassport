package com.cmg.vaccine.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.blongho.country_data.World
import com.cmg.vaccine.ImmunizationDetailActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.TestReportDetailActivity
import com.cmg.vaccine.ViewPrivateKeyActivity
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.HomeViewModel
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView

class MyViewPagerAdapter(
    private val context: Context,
private val layouts:List<Dashboard>,
    private val viewModel:HomeViewModel
) : PagerAdapter() {
    private var layoutInflater:LayoutInflater?=null
    private var privateKey:String?=null
    private var fullName:String?=null
    private var dob:String?=null

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var countrolmulticlick:Long = 0
        layoutInflater = LayoutInflater.from(context)
        val view:View = layoutInflater!!.inflate(R.layout.dashboard,container,false)
        val txtName = view.findViewById<TextView>(R.id.txt_name)
        val txtRelationShip = view.findViewById<TextView>(R.id.txt_relationship)
        val txtPassportNo = view.findViewById<TextView>(R.id.txt_passport)
        val txtNationality = view.findViewById<TextView>(R.id.txt_nationality)
        val txtIdNo = view.findViewById<TextView>(R.id.txt_id_no)
        val txtNoData = view.findViewById<TextView>(R.id.txt_nodata)
        //val txtNoDataVaccine = view.findViewById<TextView>(R.id.txt_nodata_vaccine)
        val profilePic = view.findViewById<CircleImageView>(R.id.img_profile)
        val radioGroup = view.findViewById<RadioGroup>(R.id.dashboard_radio_group)

        val radioVaccine = view.findViewById<RadioButton>(R.id.radio_vaccine)
        val radioTest = view.findViewById<RadioButton>(R.id.radio_test)

        if (!layouts[position].profileImg.isNullOrEmpty()){
            val uri = Uri.parse(layouts[position].profileImg)
            profilePic.setImageURI(uri)
        }

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
        if (!layouts[position].nationality.isNullOrEmpty()) {
            txtNationality.text = World.getCountryFrom(layouts[position].nationality!!).name
        }

        if (!layouts[position].data.isNullOrEmpty()) {
            recyclerViewVaccine.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = HomeVaccineListAdapter(layouts[position].data!!)
            }
            if (txtNoData.visibility == View.VISIBLE) {
                txtNoData.visibility = View.GONE
            }
        }else{
            if (txtNoData.visibility == View.GONE) {
                txtNoData.visibility = View.VISIBLE
            }
        }
        if (!layouts[position].dataTest.isNullOrEmpty()) {
            recyclerViewTest.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = HomeTestListAdapter(layouts[position].dataTest!!)
            }
            /*if (txtNoData.visibility == View.VISIBLE) {
                txtNoData.visibility = View.GONE
            }*/
        }/*else{
            if (txtNoData.visibility == View.GONE) {
                txtNoData.visibility = View.VISIBLE
            }
        }*/

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

        recyclerViewVaccine.addOnItemTouchListener(RecyclerViewTouchListener(context,recyclerViewVaccine,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, pos: Int) {

                Intent(context,ImmunizationDetailActivity::class.java).also {
                    it.putExtra(Passparams.VACCINE_REPORT_ID,layouts[position].data?.get(pos)?.id)
                    context.startActivity(it)
                }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_vaccine ->{
                    if (recyclerViewVaccine.visibility == View.GONE){
                        recyclerViewTest.visibility = View.GONE
                        recyclerViewVaccine.visibility = View.VISIBLE
                    }
                    if (layouts[position].data.isNullOrEmpty()){
                        txtNoData.visibility = View.VISIBLE
                    }else{
                        txtNoData.visibility = View.GONE
                    }
                    return@setOnCheckedChangeListener
                }
                R.id.radio_test ->{
                    if (recyclerViewTest.visibility == View.GONE){
                        recyclerViewVaccine.visibility = View.GONE
                        recyclerViewTest.visibility = View.VISIBLE
                    }
                    if (layouts[position].dataTest.isNullOrEmpty()){
                        txtNoData.visibility = View.VISIBLE
                    }else{
                        txtNoData.visibility = View.GONE
                    }
                    return@setOnCheckedChangeListener
                }
                R.id.radio_mykey ->{

                    if (SystemClock.elapsedRealtime() - countrolmulticlick<1000){
                        return@setOnCheckedChangeListener
                    }
                    countrolmulticlick = SystemClock.elapsedRealtime()

                    radioGroup.check(R.id.radio_vaccine)
                    privateKey = layouts[position].privateKey
                    fullName = layouts[position].fullName

                    dob = layouts[position].dob
                    //if (layouts[position].privateKey.isNullOrEmpty()) {
                        if (layouts[position].relationShip.equals(Passparams.PARENT, true)) {
                            viewModel.getPatientPrivateKey()
                        } else {
                            viewModel.getDependentPrivateKey(layouts[position].subId!!)
                        }
                    /*}else{
                        navigateToViewPrivateKey(context,privateKey,fullName,dob!!)
                    }*/
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

    private fun navigateToViewPrivateKey(context: Context,privateKey:String?,fullName:String?,dob:String){
        Intent(context,ViewPrivateKeyActivity::class.java).also {
            it.putExtra(Passparams.PRIVATEKEY, privateKey)
            it.putExtra(Passparams.USER_NAME, fullName)
            it.putExtra(Passparams.USER_DOB, dob)
            /*it.putExtra(Passparams.RELATIONSHIP, relationShip)
            it.putExtra(Passparams.SUBSID, subId)*/
            context.startActivity(it)
        }
    }

    fun refreshItem(newList:ArrayList<Dashboard>){
        //viewModel.loadData()
        /*val arrayList = arrayListOf<Dashboard>()
        arrayList.addAll(newList)*/
        /*layouts.clear()
        layouts.addAll(newList)
        notifyDataSetChanged()*/
    }
}