package com.cmg.vaccine.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.ChangeLanguageActivity
import com.cmg.vaccine.ChangePasswordActivity
import com.cmg.vaccine.LoginActivity
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.FragmentSettingsBinding

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

        binding.layoutLogout.setOnClickListener {
            showAlertForLogout()
        }
    }

    private fun showAlertForLogout(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Are you sure you want to log out?")
            .setTitle("Log Out").setCancelable(false).setPositiveButton("YES"
            ) { dialog, which ->
                Intent(requireContext(), LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    requireContext().startActivity(it)
                }
            }.setNegativeButton("CANCEl"
            ) { dialog, which -> dialog?.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}