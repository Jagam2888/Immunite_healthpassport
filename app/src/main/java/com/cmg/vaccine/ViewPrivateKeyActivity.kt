package com.cmg.vaccine

import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityViewPrivateKeyBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.ViewPrivateKeyViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.ViewPrivateKeyFactory
import com.google.zxing.WriterException
import my.com.immunitee.blockchainapi.utils.EncryptionUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ViewPrivateKeyActivity : BaseActivity(),KodeinAware,SimpleListener {
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

        viewModel.listener = this


        /*viewModel.getPrivateKey()

        viewModel.privateKey.observe(this, Observer { key ->
            privateKey = key
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermission()) {
                    generateQRCode(privateKey!!)
                } else {
                    requestPermission()
                }
            }
        })*/

        val userName = intent.extras?.getString(Passparams.USER_NAME,"")
        viewModel._userName.value = userName

        val relationShip = intent.extras?.getString(Passparams.RELATIONSHIP,"")
        val subId = intent.extras?.getString(Passparams.SUBSID,"")

        privateKey = intent.extras?.getString(Passparams.PRIVATEKEY,"")
        if (!privateKey.isNullOrEmpty()) {
            val timeStamp = System.currentTimeMillis().toString()
            val encryptMasterKeyValue = encryptMasterKey(timeStamp)
            val encryptPrivateKey = resources.getString(R.string.app_name)+"|"+encryptMasterKeyValue+"|"+encryptPrivateKey(privateKey!!,timeStamp)!!
            Log.d("encrypt_pk",encryptPrivateKey)
            generateQRCode(encryptPrivateKey)
        }else{
            if (relationShip.equals(Passparams.PARENT,true)) {
                viewModel.getPatientPrivateKey()
            }else{
                viewModel.getDependentPrivateKey(subId!!)
            }
            viewModel.privateKey.observe(this, Observer { privateKey->
                if (!privateKey.isNullOrEmpty()){
                    val timeStamp = System.currentTimeMillis().toString()
                    val encryptMasterKeyValue = encryptMasterKey(timeStamp)
                    val encryptPrivateKey = resources.getString(R.string.app_name)+"|"+encryptMasterKeyValue+"|"+encryptPrivateKey(privateKey,timeStamp)!!
                    Log.d("encrypt_pk",encryptPrivateKey)
                    generateQRCode(encryptPrivateKey)
                }
            })
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                generateQRCode(privateKey!!)
            } else {
                requestPermission()
            }
        }*/

        /*viewModel.privateKey.observe(this, Observer { privateKey->
            if (!privateKey.isNullOrEmpty()){
                generateQRCode(privateKey)
            }
        })*/

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        startTimer()


        /*val decryptMasterKeyValue = decryptMasterKey(encrptMasterKeyValue!!)
        Log.d("masterkey_1",encrptMasterKeyValue!!)
        Log.d("masterkey_2",decryptMasterKeyValue!!)

        Log.d("key_2",resources.getString(R.string.app_name)+"|"+encryptMasterKey(timeStamp)+"|"+encryptPrivateKey(timeStamp)!!)*/


        /*Log.d("key_1",privateKey!!)
        Log.d("key_2",encryptPrivateKey(privateKey!!)!!)
        decryptionPrivateKey()*/

    }

    private fun encryptPrivateKey(pKey: String,timeStamp: String):String?{

        return EncryptionUtils.encrypt(pKey,timeStamp)
    }

    private fun encryptMasterKey(timeStamp:String):String?{
        return EncryptionUtils.encrypt(timeStamp,"")
    }

   /* private fun decryptMasterKey(encryPtKey:String):String?{
        return EncryptionUtils.decrypt(encryPtKey,"")
    }*/

    /*private fun decryptionPrivateKey(){
        val minutes = 1614161650312
        var decrypt = EncryptionUtils.decrypt(encryptPrivateKey(privateKey!!),"28071988")

        if (decrypt.isNullOrEmpty())
            decrypt = "null"
        //val decrypt = EncryptionUtils.decrypt(encryptPrivateKey(privateKey!!),"1614161650312")
        Log.d("key_3",decrypt)
    }*/

    private fun startTimer(){
        val timer = object : CountDownTimer(300000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val value = millisUntilFinished / 1000
                val formatted = "${(value / 60).toString().padStart(2, '0')} : ${(value % 60).toString().padStart(2, '0')}"
                binding.txtTimer.text = formatted
            }

            override fun onFinish() {
                finish()
            }
        }
        timer.start()
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
                    val accepted: Boolean =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (accepted) {
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

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        finish()
    }
}