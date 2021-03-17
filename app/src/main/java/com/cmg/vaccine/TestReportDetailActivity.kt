package com.cmg.vaccine

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityTestReportDetailBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.hide
import com.cmg.vaccine.util.show
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.VaccineAndTestViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.VaccineAndTestModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.lang.Exception

class TestReportDetailActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityTestReportDetailBinding
    private lateinit var viewModel:VaccineAndTestViewModel

    private val factory:VaccineAndTestModelFactory by instance()

    companion object{
        const val WRITE_STORAGE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_report_detail)
        viewModel = ViewModelProvider(this,factory).get(VaccineAndTestViewModel::class.java)

        binding.viewmodel = viewModel

        binding.lifecycleOwner = this

        viewModel.listener = this

        val testReport = intent.extras?.getString(Passparams.TEST_REPORT_ID,"")
        viewModel.loadData(testReport!!)




        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnFileDownload.setOnClickListener {
            //viewModel.download()
            //val fileOutputStream = openFileOutput()
            /*if (checkPermission()) {
                downLoadFile()
            }else{
                requestPermission()
            }*/
        }
    }

    fun downLoadFile(){
        val fileName = "LAB_A06M100004"
        val directory = File(filesDir,"files")
        if (!directory.exists())
            directory.mkdir()
        val makeFile = File(directory, "$fileName.pdf")
        val downLoadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val uri = Uri.parse("http://10.1.1.150:6001/gp-module-lab/ext/labpdf?labOrderTxn=LAB_A06M100004")
        val request = DownloadManager.Request(uri).setDestinationUri(Uri.fromFile(makeFile))
                .setDescription("Downloading")
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);
        val downloadId = downLoadManager.enqueue(request)
    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_STORAGE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            WRITE_STORAGE -> {
                if(grantResults.isNotEmpty()){
                    val cameraAccepted:Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if(cameraAccepted){
                        downLoadFile()
                        /*mScannerView.setResultHandler(this)
                        mScannerView.startCamera()*/

                    }else{
                        toast("Permission Denied, You cannot access and camera")
                    }
                }
            }
        }
    }

    private fun writeFile(input: InputStream, fileName:String) {
        //Couritnes.main {
        val directory = File(filesDir,"files")
        if (!directory.exists())
            directory.mkdir()

        try {
            val makeFile = File(directory, "$fileName.pdf")
            val fos = FileOutputStream(makeFile)
            fos.use {output->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
                fos.close()
            }
            //listener?.onSuccess("success")
        }catch (e:Exception){
            e.printStackTrace()
        }
        //}

    }

    override fun onStarted() {
        show(binding.progressBar)
    }

    override fun onSuccess(msg: String) {
        hide(binding.progressBar)
        toast(msg)
        //writeFile(viewModel.inputStream.value!!,"LAB_A06M100004")

    }

    override fun onFailure(msg: String) {
        hide(binding.progressBar)
        toast(msg)
    }
}