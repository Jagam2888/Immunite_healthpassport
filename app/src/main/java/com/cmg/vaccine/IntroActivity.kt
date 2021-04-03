package com.cmg.vaccine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.cmg.vaccine.adapter.IntroViewPagerAdapter
import com.cmg.vaccine.data.IntroItem
import com.cmg.vaccine.databinding.ActivityIntroBinding
import kotlinx.android.synthetic.main.introlayout_design.view.*

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding
    var pagerAdapter: IntroViewPagerAdapter?=null
    var screenListItem:List<IntroItem>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)


        var p1 = IntroItem(
            resources.getString(R.string.blockchain_title),
            resources.getString(R.string.blockchain_desc),
            R.drawable.ic_blockchain
        )
        var p2 = IntroItem(
            resources.getString(R.string.accessibility_title),
            resources.getString(R.string.accessibility_desc),
            R.drawable.ic_accessibility
        )
        var p3 = IntroItem(
            resources.getString(R.string.booking_title),
            resources.getString(R.string.booking_desc),
            R.drawable.ic_test_booking
        )
        var p4 = IntroItem(
            resources.getString(R.string.scan_go_title),
            resources.getString(R.string.scan_go_desc),
            R.drawable.ic_scan_go
        )

        var screenList = listOf(p1, p2, p3, p4)



        binding.introViewPager.also {
            pagerAdapter = IntroViewPagerAdapter(this, screenList)
            it.adapter = pagerAdapter
            screenListItem = screenList
            if (!screenList.isNullOrEmpty()) {
                addBottomDots(0)
            }

        }



        binding.introViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.e("position",position.toString())
                Log.e("size",screenList.size.toString())
                if (position+1 == screenList.size) {
                    binding.btnStart.visibility = View.VISIBLE
                } else
                    binding.btnStart.visibility = View.INVISIBLE
            }

            override fun onPageSelected(position: Int) {
                  addBottomDots(position)
                //pagerPos = position
                //viewModel.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.btnStart.setOnClickListener {
            Intent(this, TutorialActivity::class.java).also {
                startActivity(it)
            }

        }
    }

    fun addBottomDots(currentPage : Int){
        var currentSize= screenListItem!!.size
        var dotsView = arrayOfNulls<View>(currentSize)
        binding.layoutDots?.removeAllViews()

        for(i in dotsView?.indices){
            dotsView[i] = View(this)
            val dotsViewParams = LinearLayout.LayoutParams(20,20)
            dotsViewParams.setMargins(20,0,0,0)
            dotsView[i]!!.layoutParams = dotsViewParams
            dotsView[i]!!.setBackgroundResource(R.drawable.rectangle_inactive)
            dotsView[i]!!
            binding.layoutDots?.addView(dotsView[i])
        }

        if(dotsView!!.isNotEmpty()){
            dotsView[currentPage]!!.setBackgroundResource(R.drawable.rectangle_inactive)
            dotsView[currentPage]!!.scaleX=1.2f
            dotsView[currentPage]!!.scaleY=1.2f
        }
    }
}