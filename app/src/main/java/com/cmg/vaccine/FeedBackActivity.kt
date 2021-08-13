package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.adapter.FeedBackListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityFeedBackBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.viewmodel.FeedBackViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.FeedBackViewModelFactory
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class FeedBackActivity : BaseActivity(),KodeinAware,SimpleListener {

    private lateinit var binding:ActivityFeedBackBinding
    override val kodein by kodein()

    private lateinit var viewModel: FeedBackViewModel

    private val factory: FeedBackViewModelFactory by instance()

    private lateinit var feedBackAdapter:FeedBackListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feed_back)
        viewModel = ViewModelProvider(this,factory).get(FeedBackViewModel::class.java)

        binding.viewmodel = viewModel

        val new = getString(R.string.new_)
        val inProgress = getString(R.string.in_progress)
        val solve = getString(R.string.solve)

        viewModel.feedbackStatus.value = inProgress

        binding.btnAddFeedback.setOnSingleClickListener{
            Intent(this,AddFeedbackActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        viewModel.getFeedBackListFromAPI()

        //viewModel.getFeedBackList(inProgress)

        feedBackAdapter = FeedBackListAdapter(this,viewModel)
        binding.adapter = feedBackAdapter

        /*binding.recyclerView.also {
            feedBackAdapter = FeedBackListAdapter(this,viewModel)
            it.adapter = feedBackAdapter
        }*/



        viewModel.feedBackList.observe(this,{feedBackList->
            feedBackAdapter.list = feedBackList
        })

        binding.recyclerView.addOnItemTouchListener(RecyclerViewTouchListener(this,binding.recyclerView,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                Intent(this@FeedBackActivity,FeedbackDetailActivity::class.java).also {
                    it.putExtra(Passparams.CASE_NO,viewModel.feedBackList.value?.get(position)?.caseNo)
                    startActivity(it)
                }

            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))



        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(new))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(inProgress))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(solve))

        binding.tabLayout.getTabAt(1)?.select()

        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when(tab.position){
                        0 ->{
                            viewModel.getFeedBackList(new)
                        }
                        1 ->{
                            viewModel.getFeedBackList(inProgress)
                        }
                        2 ->{
                            viewModel.getFeedBackList(solve)
                        }
                        else -> viewModel.getFeedBackList(inProgress)
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

       /* binding.cardview1.setOnSingleClickListener{
            Intent(this,FeedbackDetailActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.cardview2.setOnSingleClickListener{
            Intent(this,FeedbackDetailActivity::class.java).also {
                startActivity(it)
            }
        }*/
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onFailure(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onShowToast(msg: String) {
        TODO("Not yet implemented")
    }
}