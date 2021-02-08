package com.cmg.vaccine.fragment

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.*
import com.cmg.vaccine.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.security_pin.*

class SettingsFragment : Fragment() {

    private lateinit var binding:FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.layoutAbout.setOnClickListener {
            hideMainLayout()
            binding.about.visibility = View.VISIBLE

        }

        binding.layoutSecurityPin.setOnClickListener {
            hideMainLayout()
            binding.tested.visibility = View.VISIBLE
        }

        binding.layoutHelp.setOnClickListener {
            hideMainLayout()
            binding.help.visibility = View.VISIBLE
        }

        binding.layoutAdvanced.setOnClickListener {
            hideMainLayout()
            binding.advanced.visibility = View.VISIBLE
        }

        binding.layoutChangePassword.setOnClickListener {
            Intent(context,ChangePasswordActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.layoutChangeLanguage.setOnClickListener {
            Intent(context,ChangeLanguageActivity::class.java).also {
                context?.startActivity(it)
            }
        }

        binding.layoutNotification.setOnClickListener {
            hideMainLayout()
            binding.notification.visibility = View.VISIBLE
        }

        binding.layoutPaymentMethod.setOnClickListener {
            hideMainLayout()
            binding.paymentMethod.visibility = View.VISIBLE
        }

        binding.layoutLogout.setOnClickListener {
            showAlertForLogout()
        }

        binding.imgBack.setOnClickListener {
            showMainLayout()
        }

        layout_version_relase.setOnClickListener {
            showReleaseAppVersion()
        }



    }

    private fun hideMainLayout(){
        binding.imgBack.visibility = View.VISIBLE
        binding.mainLayout.visibility = View.GONE
        binding.layoutLogout.visibility = View.GONE
    }

    private fun showMainLayout(){
        if (binding.about.visibility == View.VISIBLE) {
            binding.about.visibility = View.GONE
        }else if(binding.tested.visibility == View.VISIBLE){
            binding.tested.visibility = View.GONE
        }else if (binding.help.visibility == View.VISIBLE){
            binding.help.visibility = View.GONE
        }else if (binding.advanced.visibility == View.VISIBLE){
            binding.advanced.visibility = View.GONE
        }else if (binding.notification.visibility == View.VISIBLE){
            binding.notification.visibility = View.GONE
        }else if (binding.paymentMethod.visibility == View.VISIBLE){
            binding.paymentMethod.visibility = View.GONE
        }

        binding.mainLayout.visibility = View.VISIBLE
        binding.layoutLogout.visibility = View.VISIBLE
        binding.imgBack.visibility = View.GONE
    }

    private fun showAlertForLogout(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Are you sure you want to log out?")
            .setTitle("Log Out").setCancelable(false).setPositiveButton("YES"
            ) { dialog, which ->
                Intent(requireContext(), LoginPinActivity::class.java).also {
                    it.putExtra("isCreate",false)
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    requireContext().startActivity(it)
                }
            }.setNegativeButton("CANCEL"
            ) { dialog, which -> dialog?.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showReleaseAppVersion(){

        try {
            val packageInfo = context?.packageManager?.getPackageInfo(context?.packageName!!,0)
            val versionName = packageInfo?.versionName
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage("Version : $versionName").setTitle(R.string.app_name)
                    .setNegativeButton("CANCEL"
                    ) { dialog, which -> dialog?.dismiss() }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }catch (e:PackageManager.NameNotFoundException){
            e.printStackTrace()
        }


    }

}