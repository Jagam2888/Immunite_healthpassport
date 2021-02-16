package com.cmg.vaccine.fragment

import android.content.Context
import android.content.Intent
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
import com.cmg.vaccine.NotificationGroupListActivity
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
        viewModel = ViewModelProvider(requireActivity(),factory).get(HomeViewModel::class.java)
        binding.homeviewmodel = viewModel
        binding.lifecycleOwner = this

        //this function only for refersh page
        viewModel.setUser()

        viewModel.users.observe(viewLifecycleOwner, Observer {
            viewModel.loadData()
        })

        //viewModel.loadVaccineDetail()


        viewModel.listDashboard.observe(viewLifecycleOwner, Observer { list->
            listDashboard = list
            binding.sliderViewPager.also {
                it.adapter = MyViewPagerAdapter(requireContext(),listDashboard!!)
                addBottomDots(0)

            }

        })

        binding.notification.setOnClickListener {
            Intent(context,NotificationGroupListActivity::class.java).also {
                context?.startActivity(it)
            }
        }


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


        viewModel.currentPagerPosition.observe(viewLifecycleOwner, Observer {
            binding.sliderViewPager.setCurrentItem(it,true)
            addBottomDots(it)
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
   /* override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }*/


}