package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.adapter.SubscriptionAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivitySubscriptionBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.SubscriptionViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.SubscriptionViewModelFactory
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_verify_face_i_d.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SubscriptionActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivitySubscriptionBinding
    private lateinit var subViewModel:SubscriptionViewModel
    private lateinit var subAdapter:SubscriptionAdapter

    private val factory:SubscriptionViewModelFactory by instance()
    var isFreeTrial = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_subscription)
        subViewModel = ViewModelProvider(this,factory).get(SubscriptionViewModel::class.java)
        binding.subViewModel = subViewModel


        subAdapter = SubscriptionAdapter()
        binding.adapter = subAdapter

        subList()

        /*binding.imgSubscribeFree.setOnSingleClickListener{
            Paper.book().write(Passparams.ISSUBSCRIBE,true)
            finish()
        }

        binding.imgSubscribePay.setOnSingleClickListener{
            Intent(this,CheckOutActivity::class.java).also {
                startActivity(it)
            }
        }*/


        /*binding.radioSubscription.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_standard ->{
                    *//*Intent(this, CheckOutActivity::class.java).also {
                        startActivity(it)
                    }*//*
                    isFreeTrial = false
                    binding.btnTrial.text = resources.getString(R.string.subscribe)
                    return@setOnCheckedChangeListener
                }
                R.id.radio_premium ->{
                    isFreeTrial = false
                    binding.btnTrial.text = resources.getString(R.string.subscribe)
                    *//*Intent(this, CheckOutActivity::class.java).also {
                        startActivity(it)
                    }*//*
                    return@setOnCheckedChangeListener
                }
                R.id.radio_free ->{
                    isFreeTrial = true
                    binding.btnTrial.text = resources.getString(R.string.get_1_month_free_trail)
                }
            }
        }*/

       /* binding.btnTrial.setOnClickListener {
            if (isFreeTrial) {
                Intent(this, SignupCompleteActivity::class.java).also {
                    startActivity(it)
                }
            }else{
                Intent(this, CheckOutActivity::class.java).also {
                    startActivity(it)
                }
            }
        }*/
    }

    private fun subList(){
        subViewModel.packageList.observe(this,{
            subAdapter.subList = it
        })

        binding.recyclerView.addOnItemTouchListener(RecyclerViewTouchListener(this,binding.recyclerView,object :RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                if (subViewModel.packageList.value?.get(position)?.subsPackageAmount!! > 0){
                    Intent(this@SubscriptionActivity,CheckOutActivity::class.java).also {
                        startActivity(it)
                    }
                }else{
                    Paper.book().write(Passparams.ISSUBSCRIBE,true)
                    finish()
                }
            }

            override fun onLongClick(view: View?, position: Int) {
            }
        }))
    }

    override fun onStarted() {
        show(binding.progressCircular)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressCircular)
    }

    override fun onFailure(msg: String) {
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressCircular)
        toast(msg)
    }
}