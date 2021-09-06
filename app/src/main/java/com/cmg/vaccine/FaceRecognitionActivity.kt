package com.cmg.vaccine

import android.content.res.Configuration
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cmg.vaccine.databinding.ActivityFaceRecognitionBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.FaceRecognitionViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.FaceRecognitionViewModelFactory
import com.google.android.cameraview.CameraView
import com.tzutalin.dlib.FaceRec
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class FaceRecognitionActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityFaceRecognitionBinding
    private lateinit var viewModel:FaceRecognitionViewModel

    private val factory:FaceRecognitionViewModelFactory by instance()

    var mFaceRec:FaceRec?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceRecognitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,factory).get(FaceRecognitionViewModel::class.java)

        viewModel.simpleListener = this

        if (binding.camera != null)
            binding.camera.addCallback(mCallback)
    }

    private val mCallback: CameraView.Callback = object : CameraView.Callback() {
        override fun onCameraOpened(cameraView: CameraView) {
            Log.d("", "onCameraOpened")
        }

        override fun onCameraClosed(cameraView: CameraView) {
            Log.d("", "onCameraClosed")
        }

        override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
            Log.d(
                "",
                "onPictureTaken " + data.size
            )
            Toast.makeText(cameraView.context, "Taken", Toast.LENGTH_SHORT)
                .show()
            val bp = BitmapFactory.decodeByteArray(data, 0, data.size)
            viewModel.recognizeFace(bp)
            /*com.google.android.cameraview.demo.MainActivity.recognizeAsync().execute(bp)*/
        }
    }



    override fun onResume() {
        super.onResume()
        binding.camera.start()
        viewModel.addPeople()
    }

    override fun onPause() {
        super.onPause()
        binding.camera.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.camera.stop()
        /*if (mFaceRec != null)
            mFaceRec!!.release()*/
    }

    override fun onStarted() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onSuccess(msg: String) {
        binding.progressCircular.visibility = View.GONE
        toast(msg)
        if (msg.startsWith("1")){
            if (binding.camera != null){
                binding.camera.takePicture()
            }
        }
    }

    override fun onFailure(msg: String) {
        binding.progressCircular.visibility = View.GONE
    }

    override fun onShowToast(msg: String) {
        binding.progressCircular.visibility = View.GONE
    }
}