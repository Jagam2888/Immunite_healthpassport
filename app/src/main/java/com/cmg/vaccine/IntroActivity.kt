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
        var p3 = IntroItem(
            resources.getString(R.string.accessibility_title),
            resources.getString(R.string.accessibility_desc),
            R.drawable.ic_accessibility
        )
        var p2 = IntroItem(
            resources.getString(R.string.booking_title),
            resources.getString(R.string.booking_desc),
            R.drawable.ic_test_booking
        )
        var p4 = IntroItem(
            resources.getString(R.string.scan_go_title),
            resources.getString(R.string.scan_go_desc),
            R.drawable.ic_scan_go
        )
        var p5 = IntroItem(
                resources.getString(R.string.signup),
                resources.getString(R.string.tutorial_signup_desc),
                R.drawable.ic_tutorial_signup
        )
        var p6 = IntroItem(
                resources.getString(R.string.world_entries),
                resources.getString(R.string.tutorial_add_we_desc),
                R.drawable.ic_tutorial_add_we
        )

        var p7 = IntroItem(
                resources.getString(R.string.entry_req_title),
                resources.getString(R.string.entry_req_desc),
                R.drawable.ic_tutorial_chk_entry_req
        )
        var p8 = IntroItem(
                resources.getString(R.string.vaccine_n_test_result),
                resources.getString(R.string.tutorial_vaccine_test_desc),
                R.drawable.ic_tutorial_vac_test_result
        )

        var screenList = listOf(p1, p2, p3,p5, p6,p7,p8,p4)



        binding.introViewPager.also {
            pagerAdapter = IntroViewPagerAdapter(this, screenList)
            it.adapter = pagerAdapter
            screenListItem = screenList
            if (!screenList.isNullOrEmpty()) {
                addBottomDots(0)
            }

        }

        binding.btnNext.setOnClickListener {
            var pos=binding.introViewPager.currentItem
            Log.e("position",pos.toString())
            Log.e("size",screenList.size.toString())
            if(pos<screenList.size){
                ++pos
                binding.introViewPager.currentItem=pos
            }

            if(pos==screenList.size){
                Intent(this, WelcomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                }
            }
        }
        binding.btnSkip.setOnClickListener {
            Intent(this, WelcomeActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
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
                    binding.btnSkip.visibility = View.INVISIBLE
                } else
                    binding.btnSkip.visibility = View.VISIBLE

                if (position > 2){
                    binding.mainlayout.setBackgroundResource(R.drawable.background_transparent_intro)
                }else{
                    binding.mainlayout.setBackgroundResource(R.drawable.background_curve_shape)
                }
            }

            override fun onPageSelected(position: Int) {
                  addBottomDots(position)
                //pagerPos = position
                //viewModel.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        /*binding.btnStart.setOnClickListener {
            Intent(this, TutorialActivity::class.java).also {
                startActivity(it)
            }

        }*/
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