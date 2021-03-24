package com.cmg.vaccine.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
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
import com.cmg.vaccine.ViewPrivateKeyActivity
import com.cmg.vaccine.adapter.MyViewPagerAdapter
import com.cmg.vaccine.databinding.FragmentHomeBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.Dashboard
import com.cmg.vaccine.model.response.HomeResponse
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.HomeViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.HomeViewModelFactory
import immuniteeEncryption.EncryptionUtils
import io.paperdb.Paper
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    var list:List<HomeResponse>?=null

    private val factory:HomeViewModelFactory by instance()
    val layouts = listOf("parent","child1","child2")
    var listDashboard:List<Dashboard>?=null
    var lastClickTime:Long = 0
    var pagerAdapter:MyViewPagerAdapter?=null
    var pagerPos:Int = 0

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
        viewModel.listener = this

        /*if (!viewModel.privateKey.value.isNullOrEmpty()) {
            viewModel.loadVaccineList()
            viewModel.loadTestReportList()
        }else {
            viewModel.setUser()
        }*/
        viewModel.setUser()

        //viewModel.loadData()
        //}
        /*if (viewModel.vaccineList.value.isNullOrEmpty()) {
            viewModel.loadVaccineList()
        }else if (viewModel.testReportList.value.isNullOrEmpty()){
            viewModel.loadTestReportList()
        }*/


        //this function only for refersh page
        //viewModel.setUser()



        //viewModel.loadVaccineDetail()

        viewModel.users.observe(viewLifecycleOwner, Observer {
            viewModel.loadData()
        })



        viewModel.listDashboard.observe(viewLifecycleOwner, Observer { list->
            /*listDashboard.clear()
            listDashboard.addAll(list)*/
            listDashboard = list
            binding.sliderViewPager.also {
                pagerAdapter = MyViewPagerAdapter(requireContext(),listDashboard!!,viewModel)
                it.adapter = pagerAdapter
                if (!listDashboard.isNullOrEmpty()) {
                    addBottomDots(0)
                    Paper.book().write(Passparams.CURRENT_USER_SUBSID, listDashboard!!.get(0))
                }
            }

            //pagerAdapter?.refreshItem(listDashboard)

        })

        binding.notification.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime<1000){
                return@setOnClickListener
            }
            Log.d("onclick","come here")
            lastClickTime = SystemClock.elapsedRealtime()
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
                Paper.book().write(Passparams.CURRENT_USER_SUBSID,listDashboard?.get(position))
                //pagerPos = position
                viewModel.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })


        viewModel.currentPagerPosition.observe(viewLifecycleOwner, Observer {
            pagerPos = it
            binding.sliderViewPager.setCurrentItem(it,true)
            if (!listDashboard.isNullOrEmpty()) {
                addBottomDots(it)
            }
            Log.d("onresume","viewModel")
        })

        Log.d("server_key",decryptServerKey("zKTbKnBGBRZ7iW3/dsT23aHbYyBiAmTb28andAJfC0oS5s8pwNNFHfdfIlC5TYoP29+yQHWY7wX2wT24lIyqvckiLbMMPC6Tx6ZkKcmFZME=","2014-09-14")!!)

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

    private fun decryptServerKey(pk:String,dob:String):String?{
        return EncryptionUtils.decrypt(pk,dob)
    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        // this is for view private key screen purpose
        val privateKeyArray = msg.split("|")
        if ((!privateKeyArray.isNullOrEmpty()) and (privateKeyArray.size > 1)){
            //viewModel.loadData()
            Intent(context,ViewPrivateKeyActivity::class.java).also {
                it.putExtra(Passparams.PRIVATEKEY, privateKeyArray[0])
                it.putExtra(Passparams.USER_NAME, privateKeyArray[1])
                it.putExtra(Passparams.USER_DOB, privateKeyArray[2])
                context?.startActivity(it)
            }
        }
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        context?.toast(msg)
    }

     override fun onResume() {
        super.onResume()
         Log.d("onresume","OnResume")
         //viewModel.loadData()
         //listDashboard.clear()
         //listDashboard.addAll(viewModel.listDashboard.value!!)
         //pagerAdapter?.refreshItem(viewModel.listDashboard.value!!)
         //viewModel.setCurrentItem(0)
        //viewModel.loadData()
    }


}