package com.cmg.vaccine

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivitySacnQRBinding
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.toast
import com.google.zxing.Result
import io.paperdb.Paper
import me.dm7.barcodescanner.zxing.ZXingScannerView

class SacnQRActivity : BaseActivity(),ZXingScannerView.ResultHandler {

    private lateinit var binding:ActivitySacnQRBinding
    private lateinit var mScannerView: ZXingScannerView

    companion object{
        const val REQUEST_CAMERA = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sacn_q_r)
        mScannerView = ZXingScannerView(this)

        if (checkPermission()){
            binding.qrFrame.addView(mScannerView)
        }else{
            requestPermission()
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

    }

    override fun handleResult(rawResult: Result?) {
        if (!rawResult.toString().isNullOrEmpty()){
            Paper.book().write(Passparams.QR_CODE_VALUE,rawResult.toString())
            finish()
        }else{
            toast("Your QR code invalid")
            mScannerView.resumeCameraPreview(this)
        }
    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CAMERA
        )
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
                        binding.qrFrame.addView(mScannerView)
                        /*mScannerView.setResultHandler(this)
                        mScannerView.startCamera()*/

                    }else{
                        toast("Permission Denied, You cannot access and camera")
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        if(mScannerView == null){
            mScannerView = ZXingScannerView(this)
            binding.qrFrame.addView(mScannerView)
        }

        mScannerView.setResultHandler(this)
        mScannerView.startCamera()


    }

    override fun onDestroy() {
        super.onDestroy()
        mScannerView.stopCamera()
    }
}