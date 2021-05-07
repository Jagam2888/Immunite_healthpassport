package com.cmg.vaccine

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySuccessPopBinding

class SuccessPopUpActivity : BaseActivity() {

    private lateinit var binding:ActivitySuccessPopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_success_pop)

        binding.btnDone.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        showAlertForExit()
    }

    private fun showAlertForExit(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to Exit?")
                .setTitle(resources.getString(R.string.app_name)).setCancelable(false).setPositiveButton(
                        "YES"
                ) { dialog, which ->
                    finish()

                }.setNegativeButton(
                        "CANCEL"
                ) { dialog, which -> dialog?.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}