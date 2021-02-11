package com.cmg.vaccine.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.cmg.vaccine.R
import com.cmg.vaccine.adapter.MyViewPagerAdapter
import com.cmg.vaccine.databinding.FragmentHomeBinding
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.response.HomeResponse
import com.cmg.vaccine.viewmodel.HomeViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.HomeViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    var list:List<HomeResponse>?=null

    private val factory:HomeViewModelFactory by instance()
    val layouts = listOf("parent","child1","child2")
    var listDashboard:List<Dashboard>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        binding.homeviewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadVaccineDetail()

        viewModel.listDashboard.observe(viewLifecycleOwner, Observer { list->
            listDashboard = list
            val myViewPagerAdapter = MyViewPagerAdapter(requireContext(),listDashboard!!)
            binding.adapter = myViewPagerAdapter
            addBottomDots(0)
        })
        //loadDynamicLayouts()

        /*binding.btnViewKey.setOnClickListener {
            Intent(context,ViewPrivateKeyActivity::class.java).also {
                context?.startActivity(it)
            }
        }*/


        binding.notification.setOnClickListener {

        }

       /* binding.btnFaq.setOnClickListener {
            Intent(context,FAQTravelAdvisoryActivity::class.java).also {
                context?.startActivity(it)
            }
        }*/

        binding.sliderViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                addBottomDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun addBottomDots(currentPage : Int){

        var dotsView = arrayOfNulls<View>(listDashboard!!.size)
        binding.layoutDots?.removeAllViews()

        for(i in dotsView?.indices){
            dotsView[i] = View(context)
            val dotsViewParams = LinearLayout.LayoutParams(20,20)
            dotsViewParams.setMargins(20,0,0,0)
            dotsView[i]!!.layoutParams = dotsViewParams
            dotsView[i]!!.setBackgroundResource(R.drawable.rectangle_inactive)
            dotsView[i]!!
            binding.layoutDots?.addView(dotsView[i])
        }

        if(dotsView!!.isNotEmpty()){
            dotsView[currentPage]!!.setBackgroundResource(R.drawable.rectangle_active)
        }
    }

    private fun loadDynamicLayouts(){

        val btnLayout = RelativeLayout(context)
        val btnLayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btnLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.red))
        btnLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        btnLayoutParams.setMargins(20,20,20,20)
        btnLayout.layoutParams = btnLayoutParams
        btnLayout.setPadding(20,20,20,20)
        binding.mainlayout.addView(btnLayout)

        val btnChildLayout = LinearLayout(context)
        val btnChildParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btnChildParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        btnChildLayout.layoutParams = btnChildParams
        btnChildLayout.orientation = LinearLayout.VERTICAL
        btnLayout.addView(btnChildLayout)

        val btnChildImg = ImageView(context)
        val btnChildImgParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btnChildImg.layoutParams = btnChildImgParams
        btnChildLayout.addView(btnChildImg)

        val btnChildTextView = TextView(context)
        val btnChildTextViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btnChildTextView.layoutParams = btnChildTextViewParams
        btnChildTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        btnChildTextView.setText(requireContext().resources.getString(R.string.my_qr_code))
        btnChildLayout.addView(btnChildTextView)


    }


}