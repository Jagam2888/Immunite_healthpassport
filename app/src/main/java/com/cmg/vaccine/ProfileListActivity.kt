package com.cmg.vaccine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.DialogFragment.DependentDialogFragment
import com.cmg.vaccine.adapter.ChildListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.FragmentProfileBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.viewmodel.ProfileViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ProfileViewModelFactory
import io.paperdb.Paper
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProfileListActivity:BaseActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    var childListAdapter:ChildListAdapter?=null

    private val factory: ProfileViewModelFactory by instance()
    var lastClickTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.fragment_profile)

        viewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
        binding.profileviewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.dependentList.observe(this, Observer {list ->
            childListAdapter = ChildListAdapter(list)
            binding.recyclerViewChild.also {
                it.layoutManager = LinearLayoutManager(this)
                it.adapter = childListAdapter
            }
        })



        binding.layoutParent.setOnClickListener {
            Intent(this,ViewProfileActivity::class.java).also {
                it.putExtra(Passparams.USER, Passparams.PARENT)
                startActivity(it)
            }
        }

        binding.btnAddDependent.setOnSingleClickListener{
            Intent(this,AddDependentActivity::class.java).also {
                startActivity(it)
            }
        }

        /*binding.btnAddDependent.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastClickTime<1000){
                return@setOnClickListener
            }
            Log.d("onclick","come here")
            lastClickTime = SystemClock.elapsedRealtime()


            Intent(this,AddDependentActivity::class.java).also {
                startActivity(it)
            }
        }*/

        binding.recyclerViewChild.addOnItemTouchListener(RecyclerViewTouchListener(this,binding.recyclerViewChild,object :
            RecyclerViewTouchListener.ClickListener{
            override fun onClick(view: View?, position: Int) {
                Intent(this@ProfileListActivity,ViewProfileActivity::class.java).also {
                    //it.putExtra(Passparams.PRIVATEKEY,viewModel.dependentList.value?.get(position)?.childPrivateKey)
                    it.putExtra(Passparams.DEPENDENT_SUBID,viewModel.dependentList.value?.get(position)?.subsId)
                    it.putExtra(Passparams.USER, Passparams.DEPENDENT)
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
        viewModel.loadParentData()
        viewModel.loadChildList()
        childListAdapter?.notifyDataSetChanged()
        if (viewModel.dependentListCount.get() >= 4) {
            if (binding.btnAddDependent.visibility == View.VISIBLE){
                binding.btnAddDependent.visibility = View.GONE
            }
        }

        if (!viewModel.getProfileImage().isNullOrEmpty()){
            val uri = Uri.parse(viewModel.getProfileImage())
            binding.imgProfile.setImageURI(uri)
        }

        val isDoneAddDependent = Paper.book().read<Boolean>(Passparams.ADD_DEPENDENT_SUCCESS,false)
        if (isDoneAddDependent){
            Paper.book().write(Passparams.ADD_DEPENDENT_SUCCESS,false)
            DependentDialogFragment().show(supportFragmentManager,"Add")
        }
    }

}