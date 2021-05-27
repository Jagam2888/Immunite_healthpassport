package com.cmg.vaccine.DialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.cmg.vaccine.R
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.DialogAlertBinding
import com.cmg.vaccine.util.Passparams

class AlertDialogFragment:DialogFragment() {

    private lateinit var binding:DialogAlertBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.white_frame);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_alert,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val title = arguments?.getString(Passparams.DIALOG_TITLE)
        val msg = arguments?.getString(Passparams.DIALOG_MSG)
        val status = arguments?.getBoolean(Passparams.DIALOG_STATUS)
        val activityFinishStatus = arguments?.getBoolean(Passparams.DIALOG_CLOSE_ACTIVITY)

        if (status == true){
            binding.imgStatus.setImageResource(R.drawable.success_msg_icon)
        }else{
            binding.imgStatus.setImageResource(R.drawable.failed_msg_icon)
            binding.btnOk.visibility = View.VISIBLE
        }

        binding.btnOk.setOnSingleClickListener{
            if (activityFinishStatus == false) {
                dismiss()
            }else{
                activity?.finish()
            }
        }

        binding.title.text = title
        binding.message.text = msg
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, height)
    }
}