package com.cmg.vaccine.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern


class OTPReceiver():BroadcastReceiver() {

    fun setEditText(editText: MutableLiveData<String>?) {
        //editText?.value = txtValue.value
        Companion.txtValue = editText
    }




    override fun onReceive(context: Context?, intent: Intent?) {
        val messages: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        messages.forEach { smsMessage ->
            val message = smsMessage.messageBody
            if (!message.isNullOrEmpty()) {
                if (message.contains("Immunitee", true)) {
                    val code = parseCode(message.trim())
                    txtValue?.value = "Your OTP is $code"
                }
            }
        }

    }

    private fun parseCode(message: String): String? {
        val p= Pattern.compile("\\b\\d{6}\\b")
        val m = p.matcher(message)
        var code = ""
        while (m.find()) {
            code = m.group(0)
        }
        return code
    }

    companion object {
        private var txtValue: MutableLiveData<String>? = null
    }

}