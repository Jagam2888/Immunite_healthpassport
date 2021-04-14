package com.cmg.vaccine.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.NotificationDetailActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.adapter.NotificationListAdapter
import com.cmg.vaccine.databinding.FragmentNotificationBinding
import com.cmg.vaccine.model.response.Notification
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.util.RecyclerviewItemDecoration
import com.cmg.vaccine.viewmodel.NotificationViewModel

class NotificationFragment : Fragment() {

    private lateinit var binding:FragmentNotificationBinding
    private lateinit var viewModel:NotificationViewModel
    var list:List<Notification>?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notification,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        val notification = Notification()
        notification.msg = "Your next Covid-19 dose is due in 2 weeks!"
        notification.status = true

        val notification1 = Notification()
        notification1.msg = "Your Covid-19 vaccination is expiring\\nin 2 weeks!"
        notification1.status = true

        val notification2 = Notification()
        notification2.msg = "Your result for Covid-19 Swab Test is \\u00E2\\u0080\\u009CNot Detected\\u00E2\\u0080\\u009D!"
        notification2.status = false

        val notification3 = Notification()
        notification3.msg = "Your Covid-19 Vaccination 1st Dose is complete!"
        notification3.status = false

/*
        list = listOf(notification,notification1,notification2,notification3)

        viewModel.setNotificationList(list!!)

        viewModel.notoficationList.observe(viewLifecycleOwner, Observer { notificationList ->

            binding.recyclerviewNotification.also {
                it.layoutManager = LinearLayoutManager(context)
                var itemDecoration = DividerItemDecoration(this.activity,LinearLayout.VERTICAL)
                itemDecoration.setDrawable(context?.getDrawable(R.drawable.recyclerview_item_decoration)!!)
                it.addItemDecoration(itemDecoration)
                it.adapter = NotificationListAdapter(list!!)
            }
        })


        binding.recyclerviewNotification.addOnItemTouchListener(RecyclerViewTouchListener(context,binding.recyclerviewNotification,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                Intent(context,NotificationDetailActivity::class.java).also {
                    it.putExtra("msg", list!!.get(position).msg)
                    context?.startActivity(it)
                }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))*/

    }

}