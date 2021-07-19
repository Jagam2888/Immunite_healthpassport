package com.cmg.vaccine

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.blongho.country_data.World
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityViewProfileBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.ProfileViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ProfileViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ViewProfileActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityViewProfileBinding
    private lateinit var viewModel:ProfileViewModel
    var user:String?=null

    var lastClickEdit:Long = 0
    var lastClickHistory:Long = 0

    private val factory:ProfileViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_profile)
        viewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this

        user = intent.extras?.getString(Passparams.USER,"")



        binding.txtEditProfile.setOnSingleClickListener {

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
        binding.txtSubmitPast.setOnSingleClickListener {
            Intent(this,ImmunizationHistoryActivity::class.java).also {
                if (user.equals(Passparams.PARENT)){
                    it.putExtra(Passparams.SUBSID,viewModel.userSubId.value)
                }else{
                    it.putExtra(Passparams.SUBSID,intent.extras?.getString(Passparams.DEPENDENT_SUBID,""))
                }
                startActivity(it)
            }
        }

        binding.btnRemoveDependent.setOnSingleClickListener {
            showAlertForRemoveDependent()
        }
    }

    private fun showAlertForRemoveDependent(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(resources.getString(R.string.app_name)).setMessage(resources.getString(R.string.sure_remove_dependent)).setPositiveButton(resources.getString(R.string.remove)
        ) { dialog, which ->
            dialog?.dismiss()
            viewModel.removeDependent(intent.extras?.getString(Passparams.DEPENDENT_SUBID, "")!!)
            /*toast("Removed Successfully")
            finish()*/
        }.setNegativeButton(resources.getString(R.string.no)
        ) { dialog, which -> dialog?.dismiss() }

        alertDialog.show()
    }


    override fun onResume() {
        super.onResume()
        viewModel.user.value = user
        var profileImage:Uri?=null
        if (user == Passparams.PARENT){
            if (binding.btnRemoveDependent.visibility == View.VISIBLE)
                binding.btnRemoveDependent.visibility = View.GONE
            viewModel.loadParentData()
            if (!viewModel.getProfileImage().isNullOrEmpty()) {
                profileImage = Uri.parse(viewModel.getProfileImage())
                binding.imgProfile.setImageURI(profileImage)
            }

           /* if (!viewModel.placeBirth.value.isNullOrEmpty()) {
                binding.viewProfilePob.setCountryForNameCode(getTwoAlpha(viewModel.placeBirth.value!!))
            }*/

            /*if (!viewModel.country.value.isNullOrEmpty()){
                nationalityViewFormat.value = World.getCountryFrom(country.value!!).name

            }*/

        }else if (user == Passparams.DEPENDENT){
            viewModel.loadDependentData(intent.extras?.getString(Passparams.DEPENDENT_SUBID,"")!!)
            if (!viewModel.getDependentProfileImage(intent.extras?.getString(Passparams.DEPENDENT_SUBID,"")!!).isNullOrEmpty()) {
                profileImage = Uri.parse(viewModel.getDependentProfileImage(intent.extras?.getString(Passparams.DEPENDENT_SUBID, "")!!))
                binding.imgProfile.setImageURI(profileImage)
            }
        }
        if (viewModel.countryCode.value != null)
            binding.phoneCode.setCountryForPhoneCode(viewModel.countryCode.value!!)

    }

    override fun onStarted() {
        show(binding.progressBarViewProfile)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBarViewProfile)
        toast(msg)
        finish()
    }

    override fun onShowToast(msg: String) {
        hide(binding.progressBarViewProfile)
        toast(msg)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBarViewProfile)
        if (msg.startsWith("2")){
            val showMsg = msg.drop(1)
            showAlertDialog(resources.getString(R.string.failed), showMsg, false, supportFragmentManager)
        }else if (msg.startsWith("3")){
            val showMsg = msg.drop(1)
            showAlertDialog(showMsg, resources.getString(R.string.check_internet), false, supportFragmentManager)
        }else {
            showAlertDialog(msg, "", false, supportFragmentManager)
        }
    }
}