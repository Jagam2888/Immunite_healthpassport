package com.cmg.vaccine.decryptTesting

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cmg.vaccine.R
import com.google.zxing.Result
import immuniteeEncryption.EncryptionUtils
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QRCodeActivity : AppCompatActivity(),ZXingScannerView.ResultHandler {

    companion object{
        const val REQUEST_CAMERA = 100
    }
    private lateinit var contentFrame:FrameLayout
    var inputValue:String?=null
    private lateinit var mScannerView: ZXingScannerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sacn_q_r)
        mScannerView = ZXingScannerView(this)

        val imgBack = findViewById<ImageView>(R.id.img_back)
        contentFrame = findViewById(R.id.qr_frame)

        imgBack.setOnClickListener {
            finish()
        }

        inputValue = intent.extras?.getString("dob","")
        Log.d("dob",inputValue!!)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                contentFrame?.addView(mScannerView)
            }else{
                requestPermission()
            }
        }

        Log.d("decrypt_splash",
            decryptKeyQRCode("IMMUNITEE|7RkWR9YEVpOKxgxDwLTj7/gw3FWl3a/EYzWuNH7wHuecUU7oR/+bjIUDZWjGLfVlAPNjfTaz3uJqEwYmlQypxv35vTloLZRue51fgBYohI5JLW7UqD6JnYiTEANEDdI0W6fW8/yZClAr5NCGlODV4d6NBiYIbamhi8N+Whtjuzc=")!!)

    }

    override fun handleResult(rawResult: Result?) {
        var outPutValue:String = ""
        var qrCodeValue = rawResult.toString()
        if (!qrCodeValue.isNullOrEmpty()) {
            if (inputValue.isNullOrEmpty()) {
                outPutValue = decryptKeyQRCode(qrCodeValue)!!
            } else {
                qrCodeValue = qrCodeValue.replace("\\n","\n")
                outPutValue = decryptKeyBackUp(qrCodeValue, inputValue!!)!!
            }
        }

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Your Original Private Key")
        val edtTxt = EditText(this)
        edtTxt.setText(outPutValue)
        alertDialog.setView(edtTxt)
        .setNegativeButton("Cancel"
            ) { dialog, which ->
            dialog?.dismiss()
            finish()
        }


        alertDialog.show()

    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CAMERA -> {
                if(grantResults.isNotEmpty()){
                    val cameraAccepted:Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if(cameraAccepted){
                        contentFrame?.addView(mScannerView)

                    }else{
                        Toast.makeText(this,"Permission Denied, You cannot access and camera",Toast.LENGTH_LONG).show()
                        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                                showMessageOKCancel(
                                        "You need to allow access to both the permissions",
                                        DialogInterface.OnClickListener(function = positiveButtonClick)
                                )
                            }
                        }*/
                    }
                }
            }
        }
    }

    private fun decryptKeyQRCode(key:String):String?{
        return EncryptionUtils.decryptQrCode(key,1000)
    }
    private fun decryptKeyBackUp(key:String, dob:String):String?{
        return EncryptionUtils.decryptBackupKey(key,dob)
    }
    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        if(mScannerView == null){
            mScannerView = ZXingScannerView(this)
            contentFrame?.addView(mScannerView)
        }

        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        mScannerView.stopCamera()
    }
}