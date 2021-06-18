package com.cmg.vaccine.payment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.cmg.vaccine.R
import com.cmg.vaccine.databinding.PaymentMsgDialogBinding

class PaymentResultMessageDialog(
    context:Context,
    private val resultPaymentMessage: ResultPaymentMessage
):DialogFragment() {

    private lateinit var binding:PaymentMsgDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.payment_msg_dialog,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!resultPaymentMessage.strResultTitle.isNullOrEmpty()){
            binding.txtTitle.text = resultPaymentMessage.strResultTitle
        }

        if (!resultPaymentMessage.strResultInfo.isNullOrEmpty()){
            binding.txtInfo.text = resultPaymentMessage.strResultInfo
        }

        if (!resultPaymentMessage.strResultExtra.isNullOrEmpty()){
            binding.txtResult.text = resultPaymentMessage.strResultExtra
        }
    }
}