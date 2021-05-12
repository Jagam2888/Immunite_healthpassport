package com.cmg.vaccine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.cmg.vaccine.adapter.TutorialViewPagerAdapter
import com.cmg.vaccine.data.IntroItem
import com.cmg.vaccine.databinding.ActivityTutorialBinding

class TutorialActivity : BaseActivity() {

    private lateinit var binding: ActivityTutorialBinding
    var pagerAdapter: TutorialViewPagerAdapter?=null
    var screenListItem:List<IntroItem>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial)


        val p1 = IntroItem(
            resources.getString(R.string.signup),
            resources.getString(R.string.tutorial_signup_desc),
            R.drawable.ic_tutorial_signup
        )
        val p2 = IntroItem(
            resources.getString(R.string.world_entries),
            resources.getString(R.string.tutorial_we_desc),
            R.drawable.tut_we_img
        )
        val p3 = IntroItem(
            resources.getString(R.string.add_world_entries),
            resources.getString(R.string.tutorial_add_we_desc),
            R.drawable.ic_tutorial_add_we
        )
        val p4 = IntroItem(
            resources.getString(R.string.check_entry_requirement),
            resources.getString(R.string.tutorial_cer_desc),
                R.drawable.ic_tutorial_chk_entry_req
        )

        val p5 = IntroItem(
            resources.getString(R.string.vaccine_n_test_result),
            resources.getString(R.string.tutorial_vaccine_test_desc),
                R.drawable.ic_tutorial_vac_test_result
        )

        val p6 = IntroItem(
            resources.getString(R.string.scan_go_title),
            resources.getString(R.string.scan_go_desc),
                R.drawable.ic_scan_go
        )

        val screenList = listOf(p1,p2,p3, p4,p5,p6)



        binding.tutorialViewPager.also {
            pagerAdapter = TutorialViewPagerAdapter(this, screenList)
            it.adapter = pagerAdapter
            screenListItem = screenList
            if (!screenList.isNullOrEmpty()) {
                addBottomDots(0)
            }

        }



        binding.tutorialViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                Log.e("position",position.toString())
//                Log.e("size",screenList.size.toString())
               /* if (position+1 == screenList.size) {
                    binding.btnStart.visibility = View.VISIBLE
                } else
                    binding.btnStart.visibility = View.INVISIBLE*/
            }

            override fun onPageSelected(position: Int) {
                  addBottomDots(position)
                //pagerPos = position
                //viewModel.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.btnNext.setOnClickListener {
            var pos=binding.tutorialViewPager.currentItem
            Log.e("position",pos.toString())
            Log.e("size",screenList.size.toString())
            if(pos<screenList.size){
                ++pos
                binding.tutorialViewPager.currentItem=pos
            }
             if(pos==screenList.size){
                Intent(this, WelcomeActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        binding.btnSkip.setOnClickListener {
            Intent(this, WelcomeActivity::class.java).also {
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