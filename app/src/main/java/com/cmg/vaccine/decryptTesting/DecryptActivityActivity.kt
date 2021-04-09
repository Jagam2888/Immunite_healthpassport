package com.cmg.vaccine.decryptTesting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.cmg.vaccine.BaseActivity
import com.cmg.vaccine.R

class DecryptActivityActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decrypt_activity)

        val btnBackup = findViewById<AppCompatButton>(R.id.btn_backup)
        val btnQrCode = findViewById<AppCompatButton>(R.id.btn_qrcode)

        btnBackup.setOnClickListener {
            showAlertDialog()
        }

        btnQrCode.setOnClickListener {
            navigateQRCode("")
        }
    }

    private fun showAlertDialog(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Enter Date of Birth")
        val edtTxt = EditText(this)
        edtTxt.hint = "yyyyMMdd"
        edtTxt.inputType = InputType.TYPE_CLASS_NUMBER
        alertDialog.setView(edtTxt)
        alertDialog.setPositiveButton("Submit"
        ) { dialog, which -> if (!edtTxt.text.toString().isNullOrEmpty()) {
            dialog.dismiss()
            navigateQRCode(edtTxt.text.toString())
        }else{
            Toast.makeText(this,"You can't scan without Date of Birth", Toast.LENGTH_LONG).show()
        } }
                .setNegativeButton("Cancel"
                ) { dialog, which -> dialog?.dismiss() }


        alertDialog.show()
    }

    private fun navigateQRCode(value:String){
        Intent(this,QRCodeActivity::class.java).also {
            it.putExtra("dob",value)
            startActivity(it)
        }
    }
}