package com.cmg.vaccine

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityFaceDetectionBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.util.*
import com.cmg.vaccine.viewmodel.FaceDetectViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.FaceDetectViewModelFactory
import com.tzutalin.dlib.Constants
import kotlinx.android.synthetic.main.activity_verify_face_i_d.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import kotlin.math.roundToInt

class FaceDetectionActivity : BaseActivity(),KodeinAware,SimpleListener {
    private lateinit var binding: ActivityFaceDetectionBinding
    private lateinit var viewModel:FaceDetectViewModel
    private val factory:FaceDetectViewModelFactory by instance()
    override val kodein by kodein()

   // var lens = CameraX.LensFacing.FRONT
    val REQUEST_CODE_PERMISSION = 101
    var capture=false
    val REQUIRED_PERMISSIONS = arrayOf(
        "android.permission.CAMERA",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )
    var scaleBitmap:Bitmap?=null
    var destination: File?=null
    /*var detector: FirebaseVisionFaceDetector? = null
    var image: FirebaseVisionImage? = null
    var graphicOverlay: GraphicOverlay? = null*/


    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1
        const val BITMAP_QUALITY = 100
        const val MAX_IMAGE_SIZE = 500F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,factory).get(FaceDetectViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.simpleListener = this

        destination = File(Constants.getDLibDirectoryPath() + "/temp.jpg")


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

        binding.imgBack.setOnClickListener {
            finish()
        }

        if (checkPermission()){
            navigateCamera()
        }else{
            requestPermission()
        }

        binding.imgCircle.setOnSingleClickListener{
            if (checkPermission()){
                navigateCamera()
            }else{
                requestPermission()
            }
        }


        if(intent.getStringExtra("recent_capture")==null)
            if (checkPermission()){
                //binding.trackingTextureView.post(Runnable { startCamera() })
            }else{
                requestPermission()
            }
        else{

        }


    }

    private fun navigateCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }catch (e:ActivityNotFoundException){

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
      /*  CameraX.unbindAll()
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
        CameraX.bindToLifecycle(this, preview, imageAnalysis)*/
    }

    private fun cropImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Face ID")
            .setCropShape(CropImageView.CropShape.OVAL)
            .setFixAspectRatio(true)
            .setCropMenuCropButtonTitle("Done")
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == REQUEST_IMAGE_CAPTURE) and (resultCode == RESULT_OK)){
            val bitMap = data?.extras?.get("data") as Bitmap
            scaleBitmap = scaleDown(bitMap!!, MAX_IMAGE_SIZE,true)
            Log.d("image_detect",destination?.absolutePath!!)
            viewModel.detectFace(scaleBitmap!!,destination!!)
        }
        /*if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri = result?.uri
                binding.imgCircle.setImageURI(resultUri)
                binding.textDesc.text = resources.getString(R.string.set_up_face_id)
                binding.layoutComplete.visibility = View.VISIBLE
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result?.error
                error?.message?.let { toast(it) }
            }
        }*/

    }
    private fun scaleDown(
        realImage: Bitmap,
        maxImageSize: Float,
        filter: Boolean
    ): Bitmap? {
        val ratio = Math.min(
            maxImageSize / realImage.width,
            maxImageSize / realImage.height
        )
        val width = (ratio * realImage.width).roundToInt()
        val height = (ratio * realImage.height).roundToInt()
        return Bitmap.createScaledBitmap(
            realImage, width,
            height, filter
        )
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
                        //cropImage()
                        navigateCamera()
                        //binding.trackingTextureView.post(Runnable { startCamera() })
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

    override fun onStarted() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onSuccess(msg: String) {
        binding.progressCircular.visibility = View.GONE
        binding.imgCircle.setImageBitmap(scaleBitmap)
        val targetPath =
            Constants.getDLibImageDirectoryPath() + "/" + viewModel.userName.value + ".jpg"
        Log.d("path_image",viewModel.filePath.value!!)
        Log.d("path_name",targetPath)
        copyFile(viewModel.filePath.value!!,targetPath)
        //FileUtils.copyFile(viewModel.filePath.value!!,targetPath)
        //toast(msg)
    }

    override fun onFailure(msg: String) {
        binding.progressCircular.visibility = View.GONE
        toast(msg)
    }

    override fun onShowToast(msg: String) {
        binding.progressCircular.visibility = View.GONE
        toast(msg)
    }
}