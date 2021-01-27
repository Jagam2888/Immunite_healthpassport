package com.cmg.vaccine

import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityViewPrivateKeyBinding
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.ViewPrivateKeyViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ViewPrivateKeyFactory
import com.google.zxing.WriterException
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ViewPrivateKeyActivity : AppCompatActivity(),KodeinAware {
    override val kodein by kodein()
    private lateinit var binding:ActivityViewPrivateKeyBinding
    private lateinit var viewModel:ViewPrivateKeyViewModel

    private val factory:ViewPrivateKeyFactory by instance()

    val WRITE_EXTERNAL_STORAGE:Int = 1
    var privateKey:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_private_key)
        viewModel = ViewModelProvider(this, factory).get(ViewPrivateKeyViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.getPrivateKey()

        viewModel.privateKey.observe(this, Observer { key ->
            privateKey = key
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermission()) {
                    generateQRCode(privateKey!!)
                } else {
                    requestPermission()
                }
            }
        })

        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun generateQRCode(privateKey: String){
        if (!privateKey.isNullOrEmpty()){
            val manager = getSystemService(WINDOW_SERVICE) as WindowManager
            var display = manager.defaultDisplay
            var point = Point()
            display.getSize(point)
            val width = point.x
            val height = point.y
            var smallerDimension = if (width < height) width else height
            smallerDimension = smallerDimension * 3 / 4

            val qrgEncoder = QRGEncoder(
                privateKey,
                null,
                QRGContents.Type.TEXT,
                smallerDimension
            )
            try {
                val bitmap = qrgEncoder.encodeAsBitmap()
                binding.qrCodeGenerate.setImageBitmap(bitmap)
            }catch (e:WriterException){
                e.printStackTrace()
            }
        }else{
            toast("Your Private key is empty")
        }
    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted: Boolean =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted) {
                        generateQRCode(privateKey!!)

                    } else {
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
}