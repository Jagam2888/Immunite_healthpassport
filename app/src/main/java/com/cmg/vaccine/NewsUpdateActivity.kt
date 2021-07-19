package com.cmg.vaccine

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.adapter.NotificationListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityNewsUpdateBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.viewmodel.NotificationViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.NotificationViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class NewsUpdateActivity : BaseActivity(),KodeinAware,SimpleListener {
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

        viewModel.listener = this

        group = intent?.extras?.getString(Passparams.NOTIFICATION_FROM,null)
        if (!group.isNullOrEmpty()){
            when(group){
                Passparams.NEWS_UPDATE -> binding.actionBarTitle.text = resources.getString(R.string.news_update)
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
                    it.putExtra(Passparams.NOTIFICATION_MSG,viewModel.messageList.value?.get(position)?.message)
                    it.putExtra(Passparams.NOTIFICATION_DATE,viewModel.messageList.value?.get(position)?.date)
                    it.putExtra(Passparams.NOTIFICATION_FROM,group)
                    startActivity(it)
                }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))


        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.txtClearAll.setOnSingleClickListener{
            showAlertForClearAll()
        }

    }

    private fun showAlertForClearAll(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(resources.getString(R.string.app_name)).setMessage(resources.getString(R.string.clear_notifi_msg)).setPositiveButton(resources.getString(R.string.clear)
        ) { dialog, which ->
            dialog?.dismiss()
            viewModel.deleteNotificationByGroup(group!!)
            /*toast("Removed Successfully")
            finish()*/
        }.setNegativeButton(resources.getString(R.string.no)
        ) { dialog, which -> dialog?.dismiss() }

        alertDialog.show()
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess(msg: String) {
        if (!group.isNullOrEmpty()) {
            viewModel.loadMesaage(group!!)
        }
    }

    override fun onFailure(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onShowToast(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        if (!group.isNullOrEmpty()) {
            viewModel.loadMesaage(group!!)
        }
    }
}