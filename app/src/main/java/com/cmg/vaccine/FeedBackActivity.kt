package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityFeedBackBinding
import com.google.android.material.tabs.TabLayout

class FeedBackActivity : BaseActivity() {

    private lateinit var binding:ActivityFeedBackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feed_back)

        binding.btnAddFeedback.setOnSingleClickListener{
            Intent(this,AddFeedbackActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("New"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("In Progress"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Solve"))

        binding.tabLayout.getTabAt(1)?.select()

        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when(tab.position){
                        0 ->{
                            binding.txtStatus.text = "New"
                            binding.txtStatus1.text = "New"
                        }
                        1 ->{
                            binding.txtStatus.text = "In Progress"
                            binding.txtStatus1.text = "In Progress"
                        }
                        2 ->{
                            binding.txtStatus.text = "Solved"
                            binding.txtStatus1.text = "Solved"
                        }
                        else -> binding.txtStatus.text = "New"
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.cardview1.setOnSingleClickListener{
            Intent(this,FeedbackDetailActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.cardview2.setOnSingleClickListener{
            Intent(this,FeedbackDetailActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}