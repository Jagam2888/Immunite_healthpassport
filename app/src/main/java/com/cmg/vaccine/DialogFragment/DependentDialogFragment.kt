package com.cmg.vaccine.DialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.DialogAddDependentBinding

class DependentDialogFragment:DialogFragment() {

    private lateinit var binding:DialogAddDependentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.white_frame);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_dependent,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, height)
    }
}