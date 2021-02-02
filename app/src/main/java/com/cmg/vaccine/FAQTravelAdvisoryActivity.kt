package com.cmg.vaccine

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.adapter.FAQTravelAdapter
import com.cmg.vaccine.data.FAQListData.data
import com.cmg.vaccine.data.TravelListData.travelData
import com.cmg.vaccine.databinding.ActivityFAQTravelAdvisoryBinding
import kotlinx.android.synthetic.main.layout_faq.*
import kotlinx.android.synthetic.main.layout_travel.*

class FAQTravelAdvisoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFAQTravelAdvisoryBinding

    private var adapters: FAQTravelAdapter? = null
    private var titleList: List<String>? = null
    var indicatorWidth:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_f_a_q_travel_advisory)

        faq()

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.faq_button ->{
                    binding.txtTitle.setText("Frequently asked questions")
                    binding.txtContent.setText("Got a question? \nWe are here to answer :)")

                    /*binding.btnFaq.setBackgroundResource(R.drawable.button_green)
                    binding.btnTravel.setBackgroundResource(R.drawable.button_white)*/

                    binding.spinnerLayout.visibility = View.GONE
                    binding.searchLayout.visibility = View.VISIBLE

                    /*binding.txtFaq.setTextColor(ContextCompat.getColor(this,R.color.white))
                    binding.txtTravel.setTextColor(ContextCompat.getColor(this,R.color.gray))*/

                    faq()
                }
                R.id.travel_button ->{
                    binding.spinnerLayout.visibility = View.VISIBLE
                    binding.searchLayout.visibility = View.GONE

                    binding.txtTitle.setText("Travel\nAdvisory")
                    binding.txtContent.setText("Check the latest COVID-19 travel and testing requirements for your destination.")

                    /*binding.btnFaq.setBackgroundResource(R.drawable.button_white)
                    binding.btnTravel.setBackgroundResource(R.drawable.button_green)*/

                    /*binding.txtFaq.setTextColor(ContextCompat.getColor(this,R.color.gray))
                    binding.txtTravel.setTextColor(ContextCompat.getColor(this,R.color.white))*/
                    travel()
                }
            }
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

    }

    fun faq(){
        binding.faq.visibility = View.VISIBLE
        binding.travel.visibility = View.GONE
        val listData = data
        titleList = ArrayList(listData.keys)
        adapters = FAQTravelAdapter(this, titleList as ArrayList<String>, listData)
        listview_faq!!.setAdapter(adapters)
    }
    fun travel(){
        binding.faq.visibility = View.GONE
        binding.travel.visibility = View.VISIBLE
        val listData = travelData
        titleList = ArrayList(listData.keys)
        adapters = FAQTravelAdapter(this, titleList as ArrayList<String>, listData)
        listview_travel!!.setAdapter(adapters)
    }
}