package com.cmg.vaccine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmg.vaccine.DialogFragment.AlertDialogFragment
import com.cmg.vaccine.adapter.ChildListAdapter
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.FragmentProfileBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.RecyclerViewTouchListener
import com.cmg.vaccine.util.navigateTo
import com.cmg.vaccine.util.showAlertDialog
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
    private lateinit var childListAdapter:ChildListAdapter

    private val factory: ProfileViewModelFactory by instance()
    var lastClickTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.fragment_profile)

        viewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
        binding.profileviewmodel = viewModel
        binding.lifecycleOwner = this

        childListAdapter = ChildListAdapter()
        binding.adapter = childListAdapter

        viewModel.dependentList.observe(this, Observer {list ->
            childListAdapter.list = list
        })


        binding.btnAddDependent.setOnSingleClickListener{
            navigateTo(this,AddDependentActivity::class.java)
        }

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
        //viewModel.loadParentData()
        viewModel.loadChildList()
        childListAdapter?.notifyDataSetChanged()
        if (viewModel.dependentListCount.get() >= viewModel.allowDependentCount.get()){
            if (binding.btnAddDependent.visibility == View.VISIBLE){
                binding.btnAddDependent.visibility = View.GONE
            }
        }else{
            if (binding.btnAddDependent.visibility == View.GONE){
                binding.btnAddDependent.visibility = View.VISIBLE
            }
        }
        /*if (viewModel.dependentListCount.get() >= viewModel.allowDependentCount.get()) {
            if (binding.btnAddDependent.visibility == View.VISIBLE){
                binding.btnAddDependent.visibility = View.GONE
            }else if (binding.btnAddDependent.visibility == View.GONE){
                binding.btnAddDependent.visibility = View.VISIBLE
            }
        }*/

        /*if (!viewModel.getProfileImage().isNullOrEmpty()){
            val uri = Uri.parse(viewModel.getProfileImage())
            binding.imgProfile.setImageURI(uri)
        }*/

        val isDoneAddDependent = Paper.book().read<Boolean>(Passparams.ADD_DEPENDENT_SUCCESS,false)
        if (isDoneAddDependent){
            Paper.book().write(Passparams.ADD_DEPENDENT_SUCCESS,false)
            showAlertDialog(resources.getString(R.string.dependent_added),resources.getString(R.string.you_can_now_manage_dependent),true,supportFragmentManager)
        }
    }

}