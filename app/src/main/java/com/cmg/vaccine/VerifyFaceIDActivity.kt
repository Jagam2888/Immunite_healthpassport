package com.cmg.vaccine

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.cmg.vaccine.data.setOnSingleClickListener
import com.cmg.vaccine.databinding.ActivityVerifyFaceIDBinding
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.util.Passparams
import com.cmg.vaccine.util.toast
import com.cmg.vaccine.viewmodel.FaceRecognitionViewModel
import com.cmg.vaccine.viewmodel.viewmodelfactory.FaceRecognitionViewModelFactory
import com.tzutalin.dlib.Constants
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File

class VerifyFaceIDActivity : BaseActivity(),KodeinAware,SimpleListener {
    override val kodein by kodein()
    private lateinit var binding:ActivityVerifyFaceIDBinding
    private lateinit var viewModel:FaceRecognitionViewModel

    private val factory:FaceRecognitionViewModelFactory by instance()

    companion object{
        const val CAMERA_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyFaceIDBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,factory).get(FaceRecognitionViewModel::class.java)

        viewModel.simpleListener = this

        binding.imgBack.setOnClickListener {finish()}

        binding.faceIdBtn.setOnSingleClickListener{
            binding.layoutVerifyMenu.visibility = View.GONE
            binding.layoutFaceId.visibility = View.VISIBLE
        }

        binding.imgBackFaceId.setOnSingleClickListener{
            binding.layoutVerifyMenu.visibility = View.VISIBLE
            binding.layoutFaceId.visibility = View.GONE
        }

        binding.btnGetStart.setOnSingleClickListener{
            navigate()
        }

        binding.takePhotoBtn.setOnSingleClickListener{
            Intent(this, FaceRecognitionActivity::class.java).also {
                startActivity(it)
            }
        }

        val directory = File(Constants.getDLibDirectoryPath(this))
        if (!directory.exists())
            directory.mkdirs()

        //Log.d("local_path",cacheDir.path+File.separator+"dlib_rec_example"+File.separator)
    }

    private fun navigate(){

        if (checkPermission()) {
            viewModel.addPeople()
         }else{
             requestPermission()
         }
    }

    private fun checkPermission():Boolean{
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_CODE ->{
                if (grantResults.isNotEmpty()) {
                    val accepted: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (accepted) {
                        viewModel.addPeople()
                    } else {
                        toast("Permission Denied, You cannot access your camera")
                    }
                }
            }
        }
    }

    private fun cropImage() {
        CropImage.activity( )
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Edit Photo")
            .setCropShape(CropImageView.CropShape.OVAL)
            .setFixAspectRatio(true)
            .setCropMenuCropButtonTitle("Done")
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri = result?.uri
                var pref= PreferenceProvider(this)
                pref.saveProfileImage(resultUri.toString())
                //viewModel.profileImageUri.set(resultUri.toString())
                toast("You profile picture was successfully changed")
                Intent(this, FaceDetectionActivity::class.java).also {
                    it.putExtra("recent_capture",resultUri.toString())
                    startActivity(it)
                }
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result?.error
                error?.message?.let { toast(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStarted() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onSuccess(msg: String) {
        binding.progressCircular.visibility = View.GONE
        //toast(msg)
        //val intentValue = intent.extras?.getString(Passparams.NAVIGATE_FACE_ID)
        Intent(this, FaceDetectionActivity::class.java).also {
            //it.putExtra(Passparams.NAVIGATE_FACE_ID,intentValue)
            startActivity(it)
        }
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