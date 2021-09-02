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
import com.cmg.vaccine.util.*
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

    companion object{
        const val new = "New"
        const val inProgress = "In Progress"
        const val solve = "Solve"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feed_back)
        viewModel = ViewModelProvider(this,factory).get(FeedBackViewModel::class.java)

        binding.viewmodel = viewModel



        viewModel.feedbackStatus.value = inProgress

        binding.btnAddFeedback.setOnSingleClickListener{navigateTo(this,AddFeedbackActivity::class.java)}

        binding.imgBack.setOnClickListener {finish()}

        feedBackAdapter = FeedBackListAdapter(this,viewModel)
        binding.adapter = feedBackAdapter

        observeList()
        setTabLayout()
        recyclerViewClickListener()
    }

    private fun observeList(){
        viewModel.feedBackList.observe(this,{feedBackList->
            feedBackAdapter.list = feedBackList
        })
    }

    private fun setTabLayout(){

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
    }

    private fun recyclerViewClickListener(){
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFeedBackListFromAPI()
    }

    override fun onStarted() {
        show(binding.progressCircular)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressCircular)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressCircular)
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressCircular)
    }
}