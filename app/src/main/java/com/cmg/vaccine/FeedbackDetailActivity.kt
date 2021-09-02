package com.cmg.vaccine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.adapter.FeedBackAttachmentAdapter
import com.cmg.vaccine.databinding.ActivityFeedbackDetailBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.changeDateFormatFeedback
import com.cmg.vaccine.viewmodel.FeedBackViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.FeedBackViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class FeedbackDetailActivity : BaseActivity(),KodeinAware {

    override val kodein by kodein()
    private lateinit var binding:ActivityFeedbackDetailBinding
    private lateinit var viewModel: FeedBackViewModel
    private lateinit var feedBackAdapter:FeedBackAttachmentAdapter

    private val factory: FeedBackViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_feedback_detail)
        viewModel = ViewModelProvider(this,factory).get(FeedBackViewModel::class.java)

        binding.viewmodel = viewModel
        feedBackAdapter = FeedBackAttachmentAdapter(this)
        binding.adapter = feedBackAdapter
        binding.imgBack.setOnClickListener {finish()}

        viewModel.getFeedBackListFromAPI()

        feedBackData()
        observeFeedBackChronology()
        customLayoutManager()
        setAttachmentList()
    }

    private fun caseNo():String{
        val caseNo = intent.extras?.getString(Passparams.CASE_NO,"")
        viewModel._getFeedBackData.value = viewModel.getFeedBackDataByCaseNo(caseNo!!)
        viewModel.getFeedBackChronolgy(caseNo)
        return caseNo
    }

    private fun feedBackData(){
        viewModel.getFeedBackData.observe(this, Observer {data->
            binding.feedbackDate.text = changeDateFormatFeedback(data.createdDate)
            binding.ratingBar.count = data.rating

            if (!viewModel.userProfileList.value.isNullOrEmpty()) {
                viewModel.userProfileList.value?.forEach {profileList->
                    if (data.caseSubId == profileList.subId){
                        binding.txtUsername.text = profileList.userName
                        binding.txtRelation.text = profileList.profile+" Account"
                        return@forEach
                    }
                }
            }

        })
    }

    private fun observeFeedBackChronology(){
        viewModel.feedbackChronology.observe(this,{data->
            if (!data.comments.isNullOrEmpty()){
                binding.feedbackChronologyStatus.text = data.comments
            }
        })
    }

    private fun customLayoutManager(){
        binding.imageRecyclerview.apply {
            val customLayoutManager = LinearLayoutManager(this@FeedbackDetailActivity, RecyclerView.HORIZONTAL, false)
            layoutManager = customLayoutManager
        }
    }

    private fun setAttachmentList(){
        if (!caseNo().isNullOrEmpty()) {
            val attachementList = viewModel.getAttachementList(caseNo())
            if (!attachementList.isNullOrEmpty()) {
                feedBackAdapter.list = attachementList
            }
        }
    }
}