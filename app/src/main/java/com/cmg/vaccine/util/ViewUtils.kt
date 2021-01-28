package com.cmg.vaccine.util

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.cmg.vaccine.R
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.custom_alert_dialog.view.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun show(progressBar: ProgressBar){
    //progress_bar.visibility = View.VISIBLE
    progressBar.visibility = View.VISIBLE
}

fun hide(progressBar: ProgressBar){
    //progress_bar.visibility = View.INVISIBLE
    progressBar.visibility = View.INVISIBLE
}

fun isValidEmail(value:String):Boolean{
    return (!TextUtils.isEmpty(value) and Patterns.EMAIL_ADDRESS.matcher(value).matches())
}

fun isValidPassword(value:String):Boolean{
    return value.length >= 4
}

fun Context.alertDialog(){
    val builder = AlertDialog.Builder(this)

    val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null)
    builder.setView(dialogView).setCancelable(false).setTitle("Please Enter Your URL")

    dialogView.btn_submit.setOnClickListener {
        var url = StringBuilder()
        if(dialogView.edt_ip_address.text.isNullOrEmpty()){
            return@setOnClickListener
        }
        url.append(dialogView.edt_ip_address.text.toString())

        if(!dialogView.edt_port.text.isNullOrEmpty()){
            url.append(":")
            url.append(dialogView.edt_port.text)
        }
        Paper.book().write("url",url.toString())

    }
}

fun changeDateFormatEmail(timeMills:Long):String?{
    val simpleDateFormat = SimpleDateFormat("DD MMMM YYYY 'at' HH:mm aaa")
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeMills
    return simpleDateFormat.format(calender.time)
}