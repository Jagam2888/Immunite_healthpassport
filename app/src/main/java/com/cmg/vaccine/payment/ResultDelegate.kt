package com.cmg.vaccine.payment

import android.util.Log
import com.cmg.vaccine.CheckOutActivity

import com.ipay.IPayIHResultDelegate
import java.io.Serializable

class ResultDelegate():IPayIHResultDelegate,Serializable {


    companion object{
        const val serialVersionUID = 10001L
        private val TAG = ResultDelegate::class.java.simpleName
    }

    override fun onConnectionError(
        merchantCode: String?,
        merchantKey: String?,
        RefNo: String?,
        Amount: String?,
        Remark: String?,
        lang: String?,
        country: String?
    ) {
        CheckOutActivity.resultTitle = "CONNECTION ERROR"
        CheckOutActivity.resultInfo = "The transaction has been unsuccessful."

        var extra = ""
        extra = """
            ${extra}Merchant Code	= $merchantCode
            
            """.trimIndent()
        extra = """
            ${extra}RefNo		= $RefNo
            
            """.trimIndent()
        extra = """
            ${extra}Amount	= $Amount
            
            """.trimIndent()
        extra = """
            ${extra}Remark	= $Remark
            
            """.trimIndent()
        extra = """
            ${extra}Language	= $lang
            
            """.trimIndent()
        extra = """
            ${extra}Country	= $country
            
            """.trimIndent()
//        extra = extra + "ErrDesc\t= " + "Had connection error while connecting to IPay server";
        //        extra = extra + "ErrDesc\t= " + "Had connection error while connecting to IPay server";
        CheckOutActivity.resultExtra = extra
    }

    override fun onPaymentSucceeded(
        TransId: String?,
        RefNo: String?,
        Amount: String?,
        Remark: String?,
        AuthCode: String?
    ) {
        CheckOutActivity.resultTitle = "SUCCESS"
        CheckOutActivity.resultInfo = "You have successfully completed your transaction."

        var extra = ""
        extra = """
            ${extra}TransId	= $TransId
            
            """.trimIndent()
        extra = """
            ${extra}RefNo		= $RefNo
            
            """.trimIndent()
        extra = """
            ${extra}Amount	= $Amount
            
            """.trimIndent()
        extra = """
            ${extra}Remark	= $Remark
            
            """.trimIndent()
        extra = """
            ${extra}AuthCode	= $AuthCode
            
            """.trimIndent()
//        extra = extra + "CCName\t= " + CCName + "\n";
//        extra = extra + "CCNo\t= " + CCNo + "\n";
//        extra = extra + "S_bankname\t= " + S_bankname + "\n";
//        extra = extra + "S_country\t= " + S_country;
        //        extra = extra + "CCName\t= " + CCName + "\n";
//        extra = extra + "CCNo\t= " + CCNo + "\n";
//        extra = extra + "S_bankname\t= " + S_bankname + "\n";
//        extra = extra + "S_country\t= " + S_country;
        CheckOutActivity.resultExtra = extra
        Log.d(TAG, "Remark: $Remark")
    }

    override fun onPaymentFailed(TransId: String?,
                                 RefNo: String?,
                                 Amount: String?,
                                 Remark: String?,
                                 ErrDesc: String?) {

        CheckOutActivity.resultTitle = "FAILURE"
        CheckOutActivity.resultInfo = ErrDesc

        var extra = ""
        extra = """
            ${extra}TransId	= $TransId
            
            """.trimIndent()
        extra = """
            ${extra}RefNo		= $RefNo
            
            """.trimIndent()
        extra = """
            ${extra}Amount	= $Amount
            
            """.trimIndent()
        extra = """
            ${extra}Remark	= $Remark
            
            """.trimIndent()
        extra = """
            ${extra}ErrDesc	= $ErrDesc
            
            """.trimIndent()
//        extra = extra + "CCName\t= " + CCName + "\n";
//        extra = extra + "CCNo\t= " + CCNo + "\n";
//        extra = extra + "S_bankname\t= " + S_bankname + "\n";
//        extra = extra + "S_country\t= " + S_country;
        //        extra = extra + "CCName\t= " + CCName + "\n";
//        extra = extra + "CCNo\t= " + CCNo + "\n";
//        extra = extra + "S_bankname\t= " + S_bankname + "\n";
//        extra = extra + "S_country\t= " + S_country;
        CheckOutActivity.resultExtra = extra
        Log.d(TAG, "ErrDesc: $ErrDesc")
        Log.d(TAG, "Remark: $Remark")
    }

    override fun onPaymentCanceled(
        TransId: String?,
        RefNo: String?,
        Amount: String?,
        Remark: String?,
        ErrDesc: String?
    ) {
        CheckOutActivity.resultTitle = "FAILURE"
        CheckOutActivity.resultInfo = ErrDesc

        var extra = ""
        extra = """
            ${extra}TransId	= $TransId
            
            """.trimIndent()
        extra = """
            ${extra}RefNo		= $RefNo
            
            """.trimIndent()
        extra = """
            ${extra}Amount	= $Amount
            
            """.trimIndent()
        extra = """
            ${extra}Remark	= $Remark
            
            """.trimIndent()
        extra = """
            ${extra}ErrDesc	= $ErrDesc
            
            """.trimIndent()
//        extra = extra + "CCName\t= " + CCName + "\n";
//        extra = extra + "CCNo\t= " + CCNo + "\n";
//        extra = extra + "S_bankname\t= " + S_bankname + "\n";
//        extra = extra + "S_country\t= " + S_country;
        //        extra = extra + "CCName\t= " + CCName + "\n";
//        extra = extra + "CCNo\t= " + CCNo + "\n";
//        extra = extra + "S_bankname\t= " + S_bankname + "\n";
//        extra = extra + "S_country\t= " + S_country;
        CheckOutActivity.resultExtra = extra
        Log.d(TAG, "ErrDesc: $ErrDesc")
        Log.d(TAG, "Remark: $Remark")
    }

    override fun onRequeryResult(MerchantCode: String?, RefNo: String?, Amount: String?, Result: String?) {
        CheckOutActivity.resultTitle = "Requery Result"
        CheckOutActivity.resultInfo = ""

        var extra = ""
        extra = """
            ${extra}MerchantCode	= $MerchantCode
            
            """.trimIndent()
        extra = """
            ${extra}RefNo		= $RefNo
            
            """.trimIndent()
        extra = """
            ${extra}Amount	= $Amount
            
            """.trimIndent()
        extra = extra + "Result\t= " + kotlin.Result
        CheckOutActivity.resultExtra = extra

    }
}