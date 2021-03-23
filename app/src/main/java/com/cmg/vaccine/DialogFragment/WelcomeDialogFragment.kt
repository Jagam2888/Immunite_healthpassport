package com.cmg.vaccine.DialogFragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.WelcomeDialogBinding
import com.cmg.vaccine.util.checkGoogleServices
import com.cmg.vaccine.util.toast

class WelcomeDialogFragment:DialogFragment() {

    private lateinit var binding:WelcomeDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.white_frame);
        binding = DataBindingUtil.inflate(inflater, R.layout.welcome_dialog,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        context?.checkGoogleServices()
    }

    override fun onStart() {
        super.onStart()
        /*val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)*/
    }
}