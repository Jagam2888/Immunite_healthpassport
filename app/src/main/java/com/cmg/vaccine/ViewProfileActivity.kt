package com.cmg.vaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityViewProfileBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.viewmodel.ProfileViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ProfileViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ViewProfileActivity : AppCompatActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:ActivityViewProfileBinding
    private lateinit var viewModel:ProfileViewModel
    var user:String?=null

    private val factory:ProfileViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_profile)
        viewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
        binding.viewModel = viewModel

        user = intent.extras?.getString(Passparams.USER,"")


        binding.txtEditProfile.setOnClickListener {

            if (user == Passparams.PARENT){
                Intent(this,EditProfileActivity::class.java).also {
                    startActivity(it)
                }
            }else if (user == Passparams.DEPENDENT){
                Intent(this,EditDependentProfileActivity::class.java).also {
                    it.putExtra(Passparams.DEPENDENT_SUBID,intent.extras?.getString(Passparams.DEPENDENT_SUBID,"")!!)
                    startActivity(it)
                }
            }
        }

        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.user.value = user
        if (user == Passparams.PARENT){
            viewModel.loadParentData()
        }else if (user == Passparams.DEPENDENT){
            viewModel.loadDependentData(intent.extras?.getString(Passparams.DEPENDENT_SUBID,"")!!)
        }
        if (viewModel.countryCode.value != null)
            binding.phoneCode.setCountryForPhoneCode(viewModel.countryCode.value!!)
    }
}