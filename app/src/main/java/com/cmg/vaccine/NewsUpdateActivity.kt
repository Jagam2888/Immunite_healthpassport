package com.cmg.vaccine

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.adapter.NotificationListAdapter
import com.cmg.vaccine.databinding.ActivityNewsUpdateBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.viewmodel.NotificationViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.NotificationViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class NewsUpdateActivity : BaseActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:ActivityNewsUpdateBinding
    private lateinit var viewModel:NotificationViewModel
    private val factory:NotificationViewModelFactory by instance()

    var group:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_news_update)
        viewModel = ViewModelProvider(this,factory).get(NotificationViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        group = intent?.extras?.getString(Passparams.NOTIFICATION_FROM,null)
        if (!group.isNullOrEmpty()){
            when(group){
                Passparams.NEWS_UPDATE -> binding.actionBarTitle.text = resources.getString(R.string.news)
                Passparams.REGULATORY_UPDATE -> binding.actionBarTitle.text = resources.getString(R.string.regulatory)
                Passparams.ADVISARY_ALERT -> binding.actionBarTitle.text = resources.getString(R.string.advisory)
            }



            viewModel.messageList.observe(this, Observer { list->
                binding.notificationRecyclerView.also {
                    it.layoutManager = LinearLayoutManager(this)
                    it.adapter = NotificationListAdapter(list)
                }

            })
        }

        binding.notificationRecyclerView.addOnItemTouchListener(RecyclerViewTouchListener(this,binding.notificationRecyclerView,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                viewModel.updateNotificationReadStatus(viewModel.messageList.value?.get(position)?.id!!)
                Intent(this@NewsUpdateActivity,NotificationDetailActivity::class.java).also {
                    it.putExtra("msg",viewModel.messageList.value?.get(position)?.message)
                    startActivity(it)
                }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))


        binding.imgBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        if (!group.isNullOrEmpty()) {
            viewModel.loadMesaage(group!!)
        }
    }
}