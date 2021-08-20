package com.cmg.vaccine

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityCheckOutBinding
import com.cmg.vaccine.payment.PaymentResultMessageDialog
import com.cmg.vaccine.payment.ResultDelegate
import com.cmg.vaccine.payment.ResultPaymentMessage
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.viewmodel.CheckOutViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.CheckOutViewModelFactory
import com.ipay.IPayIH
import com.ipay.IPayIHPayment
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class CheckOutActivity : BaseActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:ActivityCheckOutBinding
    private lateinit var viewModel:CheckOutViewModel



    private val factory:CheckOutViewModelFactory by instance()

    var packageCode:String?=null

    companion object{
        var resultTitle: String? = null
        var resultInfo: String? = null
        var resultExtra: String? = null
        private const val REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_check_out)
        viewModel = ViewModelProvider(this,factory).get(CheckOutViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnPayment.setOnSingleClickListener{
            proceedPayment()
        }

        packageCode = intent.extras?.getString(Passparams.PACKAGE_CODE,"")
    }

    private fun proceedPayment(){
        val iPayIHPayment = IPayIHPayment()
        iPayIHPayment.merchantCode = Passparams.MERCHANT_CODE
        iPayIHPayment.merchantKey = Passparams.MERCHANT_KEY
        iPayIHPayment.currency = Passparams.PAYMENT_CURRENCY
        iPayIHPayment.refNo = viewModel.getSubID()+"_"+packageCode
        iPayIHPayment.amount = "1.0"
        iPayIHPayment.prodDesc = "Subscription"
        iPayIHPayment.userName = viewModel.userData.value?.fullName
        iPayIHPayment.userEmail = viewModel.userData.value?.email
        iPayIHPayment.remark = "testing"
        iPayIHPayment.lang = Passparams.LANGUAGE
        iPayIHPayment.backendPostURL = Passparams.BACKEND_POST_URL

        val paymentIntent = IPayIH.getInstance().checkout(
            iPayIHPayment,this, ResultDelegate(),IPayIH.PAY_METHOD_CREDIT_CARD
        )
        startActivityForResult(paymentIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode != 1) or (data == null)) {
            return
        }

        val resultPaymentMsg = ResultPaymentMessage()
        if (resultTitle != null) {
            resultPaymentMsg.strResultTitle = resultTitle
            resultTitle = null
        }
        if (resultInfo != null) {
            resultPaymentMsg.strResultInfo = resultInfo
            resultInfo = null
        }

        if (resultExtra != null) {
            resultPaymentMsg.strResultExtra = resultExtra
            resultExtra = null
        }

        val paymentDialog = PaymentResultMessageDialog(
            this,
            resultPaymentMsg
        )
        paymentDialog.show(supportFragmentManager,"PAYMENT")
    }
}