package com.cmg.vaccine

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityConsentAgreementBinding
import com.cmg.vaccine.util.toast
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ConsentAgreementActivity : AppCompatActivity(),ZXingScannerView.ResultHandler {

    private lateinit var binding:ActivityConsentAgreementBinding
    private lateinit var mScannerView: ZXingScannerView

    companion object{
        const val REQUEST_CAMERA:Int = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_consent_agreement)

        mScannerView = ZXingScannerView(this)

        binding.imgBack.setOnClickListener {
            finish()
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                binding.contentframe.addView(mScannerView)
            }else{
                requestPermission()
            }
        }

        toast("Development under processing")
    }

    override fun handleResult(rawResult: Result?) {
        toast("Development under processing")
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
                        binding.contentframe.addView(mScannerView)

                    }else{
                        toast("Permission Denied, You cannot access and camera")
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

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        if(mScannerView == null){
            mScannerView = ZXingScannerView(this)
            binding.contentframe.addView(mScannerView)
        }

        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        mScannerView.stopCamera()
    }
}