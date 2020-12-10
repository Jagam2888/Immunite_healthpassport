package com.cmg.vaccine.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.cmg.vaccine.R
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.custom_alert_dialog.view.*
import java.lang.StringBuilder

fun Context.toast(message:String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun ProgressBar.show(){
    progress_bar.visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    progress_bar.visibility = View.INVISIBLE
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