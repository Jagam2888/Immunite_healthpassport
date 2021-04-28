package com.cmg.vaccine

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.cmg.vaccine.util.FaceTrackingAnalyzer
import com.cmg.vaccine.databinding.ActivityFaceRecognitionBinding
import com.cmg.vaccine.util.toast

class FaceRecognitionActivity : BaseActivity() {
    private lateinit var binding: ActivityFaceRecognitionBinding

    var lens = CameraX.LensFacing.FRONT
    val REQUEST_CODE_PERMISSION = 101
    var capture=false
    val REQUIRED_PERMISSIONS = arrayOf(
        "android.permission.CAMERA",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )
    /*var detector: FirebaseVisionFaceDetector? = null
    var image: FirebaseVisionImage? = null
    var graphicOverlay: GraphicOverlay? = null*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_face_recognition)


        /*  if(intent.getStringExtra("recent_capture")!=null){

              var recent_cap=intent.getStringExtra("recent_capture")
              val uri: Uri = Uri.parse(recent_cap)
              val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
              runFaceDetector(bitmap)
          }


          binding.camera.start()

          binding.btnSignUp.setOnClickListener {
              binding.camera.captureImage()
              binding.graphicOverlay.clear()

          }

          binding.camera.setFacing(CameraKit.Constants.FACING_FRONT);
          binding.progressBar.dashLineLength=8.0f
          binding.camera.addCameraKitListener(object : CameraKitEventListener {
              override fun onEvent(p0: CameraKitEvent?) {
                  binding.camera.captureImage()
                  Log.e("Capture", "Camera")
              }

              override fun onError(p0: CameraKitError?) {

              }

              override fun onImage(p0: CameraKitImage?) {
                  //binding.progressBar.
                  var bitmap = p0?.bitmap
                  bitmap = bitmap?.let { Bitmap.createScaledBitmap(it, binding.camera.width, binding.camera.height, false) }
                  runFaceDetector(bitmap)
                  //binding.graphicOverlay.clear()
              }

              override fun onVideo(p0: CameraKitVideo?) {

              }


          })*/


        if(intent.getStringExtra("recent_capture")==null)
            if (checkPermission()){
                binding.trackingTextureView.post(Runnable { startCamera() })
            }else{
                requestPermission()
            }
        else{

        }


    }



/*    private fun runFaceDetector(bitmap: Bitmap?) {
        val image= bitmap?.let { FirebaseVisionImage.fromBitmap(it) }
        val options=FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setMinFaceSize(0.3f)
                .enableTracking()
                .build()

        val detector=FirebaseVision.getInstance().getVisionFaceDetector(options)

        image?.let {
            detector.detectInImage(it)
                .addOnSuccessListener { result->processFaceResult(result) }
                .addOnFailureListener { e->toast("fail to process face") }
        }

    }


    var temp=0

    private fun processFaceResult(result: List<FirebaseVisionFace>) {

        for(face in result){

            if(face.trackingId!=temp){
                binding.progressBar.progress=0
                temp=face.trackingId
            }
            else{
                binding.progressBar.progress+=20
            }

        }

        if(binding.progressBar.progress==100){
            binding.layout1.visibility=View.GONE
            binding.layout2.visibility=View.VISIBLE
            binding.camera.stop()
            toast("Verify Successfully")
        }







    }*/

/*    override fun onStart() {
        super.onStart()
        binding.camera.start()
    }

    override fun onResume() {
        super.onResume()
        Log.e("Hello", "Resume")
        binding.camera.start()
    }

    override fun onPause() {
        binding.camera.stop()
        super.onPause()
    }

    override fun onStop() {
        binding.camera.stop()
        super.onStop()
    }*/


    //New test

    private fun startCamera() {
        initCamera()
    }

    private fun initCamera() {
        CameraX.unbindAll()
        val pc = PreviewConfig.Builder()
            .setTargetResolution(
                Size(
                    binding.trackingTextureView.getWidth(),
                    binding.trackingTextureView.getHeight()
                )
            )
            .setLensFacing(lens)
            .build()
        val preview = Preview(pc)
        preview.setOnPreviewOutputUpdateListener { output: Preview.PreviewOutput ->
            val vg = binding.trackingTextureView.getParent() as ViewGroup
            vg.removeView(binding.trackingTextureView)
            vg.addView(binding.trackingTextureView, 0)
            binding.trackingTextureView.setSurfaceTexture(output.surfaceTexture)
        }
        val iac = ImageAnalysisConfig.Builder()
            .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            .setTargetResolution(
                Size(
                    binding.trackingTextureView.getWidth(),
                    binding.trackingTextureView.getHeight()
                )
            )
            .setLensFacing(lens)
            .build()
        val imageAnalysis = ImageAnalysis(iac)
        imageAnalysis.setAnalyzer(
            { obj: Runnable -> obj.run() },
            FaceTrackingAnalyzer(
                binding.trackingTextureView,
                binding.trackingImageView,
                lens,
                binding.progressBar,
                binding.layout2,
                binding.layout1,
                binding.resultCapture
            )
        )
        CameraX.bindToLifecycle(this, preview, imageAnalysis)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty()) {
                    val accepted: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (accepted) {
                        binding.trackingTextureView.post(Runnable { startCamera() })
                    } else {
                        toast("Permission Denied, You cannot access your camera")
                    }
                }
            }
        }
    }




    //
    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION);
    }
}