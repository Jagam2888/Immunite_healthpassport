package com.cmg.vaccine

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.databinding.ActivityConsentAgreementBinding
import com.cmg.vaccine.util.hideKeyBoard
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
            if (binding.layoutForm.visibility == View.VISIBLE) {
                finish()
            }else if (binding.contentframe.visibility == View.VISIBLE){
                if (mScannerView != null)
                    mScannerView.stopCamera()
                binding.contentframe.removeAllViews()
                binding.contentframe.visibility = View.GONE


                if (binding.layoutForm.visibility == View.GONE) {
                    binding.layoutForm.visibility = View.VISIBLE
                }
            }
        }



        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnScanQr.setOnClickListener {
            hideKeyBoard()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkPermission()){
                    if (binding.layoutForm.visibility == View.VISIBLE)
                        binding.layoutForm.visibility = View.GONE

                    if (binding.contentframe.visibility == View.GONE) {
                        binding.contentframe.visibility = View.VISIBLE
                    }
                    binding.contentframe.addView(mScannerView)

                    if (mScannerView != null) {
                        mScannerView.setResultHandler(this)
                        mScannerView.startCamera()
                    }
                }else{
                    requestPermission()
                }
            }
        }

        binding.btnSendMykey.setOnClickListener {
            if (!binding.edtEcode.text.isNullOrEmpty()){
                if(binding.edtEcode.text.length < 6){
                    toast("Please check you eCode is Wrong")
                }else {
                    toast("Your request is being process...")
                }
            }else{
                toast("Please enter your eCode")
            }
        }

        //toast("Your request is being process...")
    }

    override fun handleResult(rawResult: Result?) {
        toast("Your request is being process...")
        if (binding.contentframe.visibility == View.VISIBLE) {
            binding.contentframe.visibility = View.GONE
        }
        binding.layoutForm.visibility = View.VISIBLE
        if (!rawResult.toString().isNullOrEmpty())
            binding.edtEcode.setText(rawResult.toString())
        //finish()
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
                        binding.layoutForm.visibility = View.GONE
                        if (binding.contentframe.visibility == View.GONE)
                            binding.contentframe.visibility = View.VISIBLE
                        binding.contentframe.addView(mScannerView)

                    }else{
                        if (binding.contentframe.visibility == View.VISIBLE) {
                            binding.contentframe.visibility = View.GONE
                        }
                        if (binding.layoutForm.visibility == View.GONE)
                            binding.layoutForm.visibility = View.VISIBLE
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